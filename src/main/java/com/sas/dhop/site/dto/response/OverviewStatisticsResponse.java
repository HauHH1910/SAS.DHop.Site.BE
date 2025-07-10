package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;

public record OverviewStatisticsResponse(
        Long totalUsers,
        Long totalBookings,
        BigDecimal totalRevenue,
        Long bookingFeedbackRating
) {
}
