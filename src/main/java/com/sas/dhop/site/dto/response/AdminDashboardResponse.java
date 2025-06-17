package com.sas.dhop.site.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AdminDashboardResponse {
  // Total counts
  private final Long totalBookings;
  private final Long totalUsers;
  private final Long totalDancers;
  private final Long totalChoreographers;

  // Revenue metrics
  private final BigDecimal totalRevenue;
  private final BigDecimal bookingRevenue;
  private final BigDecimal subscriptionRevenue;
  private final BigDecimal thisMonthRevenue;
  private final BigDecimal lastMonthRevenue;
  private final Double revenueGrowthRate;

  // Booking metrics
  private final Long activeBookings;
  private final Long completedBookings;
  private final Long canceledBookings;
  private final Long pendingBookings;

  // Time-based statistics
  private final List<BookingStatisticsResponse> dailyBookings;
  private final List<BookingStatisticsResponse> weeklyBookings;
  private final List<BookingStatisticsResponse> monthlyBookings;

  // Revenue breakdown
  private final List<RevenueStatisticsResponse> revenueByService;
  private final List<RevenueStatisticsResponse> monthlyRevenueBreakdown;

  // Additional metrics
  private final Double averageBookingValue;
  private final Long activeSubscriptions;
  private final Long expiredSubscriptions;

  // Date range for the data
  private final LocalDate dataFromDate;
  private final LocalDate dataToDate;
}