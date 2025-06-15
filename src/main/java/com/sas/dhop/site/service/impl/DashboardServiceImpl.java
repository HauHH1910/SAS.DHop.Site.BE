package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.AdminDashboardRequest;
import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.BookingFeedbackService;
import com.sas.dhop.site.service.DashboardService;
import com.sas.dhop.site.util.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	private final UserRepository userRepository;
	private final DancerRepository dancerRepository;
	private final BookingRepository bookingRepository;
	private final UserSubscriptionRepository userSubscriptionRepository;
	private final BookingFeedbackService bookingFeedbackService;
	private final DashboardMapper dashboardMapper;

	@Override
	public AdminDashboardResponse getAdminDashboard(AdminDashboardRequest request) {
		// Long totalUsers = userRepository.count();
		// Long totalDancers = dancerRepository.count();
		// Long totalChoreographers = choreographerRepository.count();
		// Long totalBookings = bookingRepository.count();
		//
		// BigDecimal totalRevenue =
		// bookingRepository.getTotalAmountOfActivatedBookings();
		//
		// List<Booking> recentBookings =
		// bookingRepository.findTop5ByOrderByBookingDateDesc();
		// List<Dancer> topDancers = dancerRepository.findTop5ByOrderByPriceDesc();
		// List<Choreographer> topChoreographers =
		// choreographerRepository.findTop5ByOrderByPriceDesc();
		//
		// return dashboardMapper.toAdminDashboardResponse(
		// totalUsers,
		// totalDancers,
		// totalChoreographers,
		// totalBookings,
		// totalRevenue,
		// recentBookings,
		// topDancers,
		// topChoreographers
		// );
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
