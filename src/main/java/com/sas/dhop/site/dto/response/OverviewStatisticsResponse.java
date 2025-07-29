package com.sas.dhop.site.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OverviewStatisticsResponse
        (
        Long totalUsers,
        Long totalBookings,
        Long totalSubscriptionRevenue,
        Long totalBookingRevenue,
        Long totalRating
        ) {
}
