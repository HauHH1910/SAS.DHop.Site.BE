package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.dto.response.UserSubscriptionResponse.mapToResponse;

import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.constant.SubscriptionPlan;
import com.sas.dhop.site.constant.UserSubscriptionStatus;
import com.sas.dhop.site.dto.request.UserSubscriptionRequest;
import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import java.time.Instant;
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
    public UserSubscriptionResponse addSubscriptionToUser(UserSubscriptionRequest request) {
        return null;
    }

    @Override
    public void updateSubscriptionStatus() {}

    @Override
    public UserSubscription findUserSubscriptionByUser(User user) {
        return userSubscriptionRepository.findByUser(user).orElseGet(() -> UserSubscription.builder()
                .user(user)
                .subscription(subscriptionService.findOrCreateSubscription(SubscriptionPlan.FREE_TRIAL))
                .fromDate(Instant.now())
                .status(statusService.findStatusOrCreated(UserSubscriptionStatus.FREE_TRIAL_USER_SUBSCRIPTION))
                .build());
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
