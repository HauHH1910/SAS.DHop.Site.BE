package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.dto.response.BookingStatisticsResponse;
import com.sas.dhop.site.dto.response.RevenueStatisticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface AdminDashboardService {

    /**
     * Get comprehensive admin dashboard data
     */
    AdminDashboardResponse getAdminDashboard();

    /**
     * Get booking statistics by date range
     */
    BookingStatisticsResponse getBookingStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * Get booking statistics by time period (week, month, year)
     */
    List<BookingStatisticsResponse> getBookingStatisticsByPeriod(String period, int count);

    /**
     * Get booking counts by status
     */
    List<BookingStatisticsResponse> getBookingCountsByStatus(List<String> statuses);

    /**
     * Get revenue statistics
     */
    RevenueStatisticsResponse getRevenueStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * Get total website revenue
     */
    Double getTotalWebsiteRevenue();

    /**
     * Get subscription revenue breakdown
     */
    List<RevenueStatisticsResponse> getSubscriptionRevenue(LocalDate startDate, LocalDate endDate);

    /**
     * Get booking revenue breakdown
     */
    List<RevenueStatisticsResponse> getBookingRevenue(LocalDate startDate, LocalDate endDate);
} 