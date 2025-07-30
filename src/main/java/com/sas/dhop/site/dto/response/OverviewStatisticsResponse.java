package com.sas.dhop.site.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.OptionalDouble;

@Builder
public record OverviewStatisticsResponse
        (
        Long totalUsers,
        Long totalBookings,
        Long totalSubscriptionRevenue,
        Long totalBookingRevenue,
        Long totalRating,
        OptionalDouble averageRating
        ) {
}
