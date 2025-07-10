package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.PaymentStatus;
import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.BookingFeedback;
import com.sas.dhop.site.model.Payment;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.service.DashboardService;
import com.sas.dhop.site.util.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final BookingRepository bookingRepository;
    private final AuthenticationService authenticationService;
    private final StatusRepository statusRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final DashboardMapper dashboardMapper;
    private final PaymentRepository paymentRepository;
    private final BookingFeedbackRepository bookingFeedbackRepository;

    @Override
    public OverviewStatisticsResponse overviewStatistics() {
        boolean isAdmin = authenticationService.authenticationChecking(RolePrefix.ADMIN_PREFIX);
        if (!isAdmin) {
            throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
        }

        long totalUser = userRepository.count();
        long totalBookings = bookingRepository.count();

        AtomicInteger totalRevenue = new AtomicInteger();

        List<Payment> payments = paymentRepository.findAll();

        payments.stream().
                filter(payment -> payment.getStatus().equals(PaymentStatus.PAID))
                .forEach(payment -> {
                    totalRevenue.addAndGet(payment.getAmount());
                });

        List<BookingFeedback> feedbacks = bookingFeedbackRepository.findAll();

        long bookingFeedbackRating = (long) feedbacks.stream()
                .mapToInt(BookingFeedback::getRating)
                .average()
                .orElse(0.0);

        return new OverviewStatisticsResponse
                (totalUser, totalBookings, BigDecimal.valueOf(totalRevenue.get()), bookingFeedbackRating);
    }

    @Override
    public List<Object> revenueAndUserGrowth() {
        return null;
    }

    @Override
    public DancerDashboardResponse getDancerDashboard(DancerDashboardRequest request) {
        // Long totalBookings = bookingRepository.countByDancerId(request.dancerId());
        // Long completedBookings =
        // bookingRepository.countByDancerIdAndStatusId(request.dancerId(), 3); //
        // Assuming 3 is completed status
        // Long pendingBookings =
        // bookingRepository.countByDancerIdAndStatusId(request.dancerId(), 1); //
        // Assuming 1 is pending status
        //
        // BigDecimal totalEarnings =
        // bookingRepository.getTotalAmountOfActivatedBookingsByDancerId(request.dancerId());
        //
        // List<Booking> upcomingBookings =
        // bookingRepository.findTop5ByDancerIdAndStatusIdOrderByStartAsc(request.dancerId(),
        // 1);
        // List<BookingFeedback> recentFeedbacks =
        // bookingFeedbackService.getFeedbackByDancerId(request.dancerId());
        //
        // UserSubscription currentSubscription =
        // userSubscriptionRepository.findByUserId(request.dancerId())
        // .orElseThrow(() -> new RuntimeException("No subscription found"));
        //
        // return dashboardMapper.toDancerDashboardResponse(
        // totalBookings,
        // completedBookings,
        // pendingBookings,
        // totalEarnings,
        // upcomingBookings,
        // recentFeedbacks,
        // currentSubscription
        // );
        return null;
    }

    @Override
    public ChoreographerDashboardResponse getChoreographerDashboard(ChoreographerDashboardRequest request) {
        // Long totalBookings =
        // bookingRepository.countByChoreographyId(request.choreographerId());
        // Long completedBookings =
        // bookingRepository.countByChoreographyIdAndStatusId(request.choreographerId(),
        // 3);
        // Long pendingBookings =
        // bookingRepository.countByChoreographyIdAndStatusId(request.choreographerId(),
        // 1);
        //
        // BigDecimal totalEarnings =
        // bookingRepository.getTotalAmountOfActivatedBookingsByChoreographyId(request.choreographerId());
        //
        // List<Booking> upcomingBookings =
        // bookingRepository.findTop5ByChoreographyIdAndStatusIdOrderByStartAsc(request.choreographerId(),
        // 1);
        // List<BookingFeedback> recentFeedbacks =
        // bookingFeedbackService.getFeedbackByChoreographer(request.choreographerId());
        //
        // UserSubscription currentSubscription =
        // userSubscriptionRepository.findByUserId(request.choreographerId())
        // .orElseThrow(() -> new RuntimeException("No subscription found"));
        //
        // List<Dancer> topDancers = dancerRepository.findTop5ByOrderByPriceDesc();
        //
        // return dashboardMapper.toChoreographerDashboardResponse(
        // totalBookings,
        // completedBookings,
        // pendingBookings,
        // totalEarnings,
        // upcomingBookings,
        // recentFeedbacks,
        // currentSubscription,
        // topDancers
        // );
        return null;
    }
}
