package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.dto.response.UserSubscriptionResponse.mapToResponse;

import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.constant.SubscriptionPlan;
import com.sas.dhop.site.constant.UserSubscriptionStatus;
import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[User Subscription Service]")
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final StatusService statusService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookingRepository bookingRepository;
    private final AuthenticationService authenticationService;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<UserSubscriptionResponse> getSubscriptionStatus() {

        List<UserSubscription> list = userSubscriptionRepository.findAllByUser(userService.getLoginUser());

        List<UserSubscriptionResponse> responsesList = new ArrayList<>();

        list.forEach(subscription -> {
            log.info(
                    "[get subscription status] - [{}]",
                    subscription.getSubscription().getName());
            responsesList.add(mapToResponse(subscription));
        });
        return responsesList;
    }

    @Override
    public void addOrForceToBuySubscription(Integer userId) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        boolean isDancer = authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX);
        boolean isChoreography = authenticationService.authenticationChecking(RolePrefix.CHOREOGRAPHY_PREFIX);

        if (!isDancer && !isChoreography) {
            log.info("[Add Or Force To Buy Subscription] - Mày không có quyền vào đây nigga [{}]", user.getName());
            throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
        }

        UserSubscription userSubscription =
                userSubscriptionRepository.findByUser(user).orElse(null);

        if (userSubscription != null) {
            Dancer dancer = dancerRepository
                    .findByUser(userSubscription.getUser())
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_DANCER));

            long counted = bookingRepository.countBookingByDancerAndBookingDateBetween(
                    dancer, userSubscription.getFromDate(), userSubscription.getToDate());
            log.info("[Add Or Force To Buy Subscription] - Mày dùng hết [{}] lần rồi nigga", counted);
            if (!checkCurrentSubscriptionAndNumberOfBookingAccepted(userSubscription, counted)) {
                log.info(
                        "[Add Or Force To Buy Subscription] - Mua gói subscription mới đi [{}] ủng hộ người nghèo",
                        userSubscription.getUser().getName());
                throw new BusinessException(ErrorConstant.SUBSCRIPTION_ENDED);
            }
        } else {
            Subscription freeTrialSubscription =
                    subscriptionService.findOrCreateSubscription(SubscriptionPlan.FREE_TRIAL);
            log.info(
                    "[Add Or Force To Buy Subscription] - Cho mày dùng thử gói [{}] nha nigga",
                    SubscriptionPlan.FREE_TRIAL);
            userSubscription = UserSubscription.builder()
                    .user(user)
                    .fromDate(LocalDateTime.now())
                    .toDate(LocalDateTime.now().plusDays(freeTrialSubscription.getDuration()))
                    .status(statusService.findStatusOrCreated(UserSubscriptionStatus.FREE_TRIAL_USER_SUBSCRIPTION))
                    .subscription(freeTrialSubscription)
                    .build();

            userSubscriptionRepository.save(userSubscription);
        }
    }

    private static boolean checkCurrentSubscriptionAndNumberOfBookingAccepted(
            UserSubscription userSubscription, long counted) {
        long maxBookings =
                switch (userSubscription.getSubscription().getStatus().getStatusName()) {
                    case SubscriptionPlan.FREE_TRIAL -> 3;
                    case SubscriptionPlan.STANDARD_3MONTHS -> 10;
                    case SubscriptionPlan.STANDARD_MONTHLY -> 20;
                    case SubscriptionPlan.UNLIMITED_YEARLY -> Long.MAX_VALUE;
                    default -> throw new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND);
                };
        return counted < maxBookings;
    }

    @Override
    public void updateSubscriptionStatus() {}

    @Override
    public UserSubscription findUserSubscriptionByUser(User user) {
        return null;
    }

    @Override
    public Integer countBookingFromUserSubscription(UserSubscription userSubscription) {
        User user = userSubscription.getUser();
        Subscription currentSubscription = userSubscription.getSubscription();
        String planName = currentSubscription.getStatus().getStatusName();

        List<Booking> bookings;

        if (authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX)) {
            Dancer dancer = dancerRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_DANCER));
            bookings = bookingRepository.findAllByDancer(dancer);
        } else if (authenticationService.authenticationChecking(RolePrefix.CHOREOGRAPHY_PREFIX)) {
            Choreography choreography = choreographyRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_FOUND_CHOREOGRAPHY));
            bookings = bookingRepository.findAllByChoreography(choreography);
        } else {
            return 0;
        }

        int bookingCount = bookings.size();
        int allowedLimit = getSubscriptionBookingLimit(planName);

        if (bookingCount >= allowedLimit) {
            throw new BusinessException(ErrorConstant.SUBSCRIPTION_ENDED);
        }

        return bookingCount;
    }

    private int getSubscriptionBookingLimit(String planName) {
        return switch (planName) {
            case SubscriptionPlan.FREE_TRIAL -> 3;
            case SubscriptionPlan.STANDARD_3MONTHS -> 20;
            case SubscriptionPlan.STANDARD_MONTHLY -> 10;
            default -> Integer.MAX_VALUE; // Unlimited
        };
    }
}
