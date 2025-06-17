package com.sas.dhop.site.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record AdminDashboardResponse(
        // Total counts
        Long totalBookings,
        Long totalUsers,
        Long totalDancers,
        Long totalChoreographers,
        
        // Revenue metrics
        BigDecimal totalRevenue,
        BigDecimal bookingRevenue,
        BigDecimal subscriptionRevenue,
        BigDecimal thisMonthRevenue,
        BigDecimal lastMonthRevenue,
        Double revenueGrowthRate,
        
        // Booking metrics
        Long activeBookings,
        Long completedBookings,
        Long canceledBookings,
        Long pendingBookings,
        
        // Time-based statistics
        List<BookingStatisticsResponse> dailyBookings,
        List<BookingStatisticsResponse> weeklyBookings,
        List<BookingStatisticsResponse> monthlyBookings,
        
        // Revenue breakdown
        List<RevenueStatisticsResponse> revenueByService,
        List<RevenueStatisticsResponse> monthlyRevenueBreakdown,
        
        // Additional metrics
        Double averageBookingValue,
        Long activeSubscriptions,
        Long expiredSubscriptions,
        
        // Date range for the data
        LocalDate dataFromDate,
        LocalDate dataToDate
) {} 