package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record AdminDashboardResponse(
        Long totalUsers,
        Long totalDancers,
        Long totalChoreographers,
        Long totalBookings,
        BigDecimal totalRevenue


) {}
