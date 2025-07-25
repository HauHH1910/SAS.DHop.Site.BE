package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ChoreographerDashboardResponse(
        Long totalBookings,
        Long completedBookings,
        Long pendingBookings,
        BigDecimal totalEarnings,
        List<BookingResponse> upcomingBookings,
        List<BookingFeedbackResponse> recentFeedbacks,
        UserSubscriptionResponse currentSubscription,
        List<DancerResponse> topDancers) {}
