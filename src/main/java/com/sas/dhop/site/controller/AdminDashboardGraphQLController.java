package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.dto.response.BookingStatisticsResponse;
import com.sas.dhop.site.dto.response.RevenueStatisticsResponse;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "[Admin Dashboard GraphQL Controller]")
public class AdminDashboardGraphQLController {

    private final AdminDashboardService adminDashboardService;
    private final BookingRepository bookingRepository;

    /**
     * Get comprehensive admin dashboard data
     */
    @QueryMapping
    public AdminDashboardResponse getAdminDashboard() {
        log.info("Fetching admin dashboard data");
        return adminDashboardService.getAdminDashboard();
    }

    /**
     * Get booking statistics by date range
     */
    @QueryMapping
    public BookingStatisticsResponse getBookingStatistics(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        log.info("Fetching booking statistics from {} to {}", startDate, endDate);
        return adminDashboardService.getBookingStatistics(startDate, endDate);
    }

    /**
     * Get booking statistics by time period (week, month, year)
     */
    @QueryMapping
    public List<BookingStatisticsResponse> getBookingStatisticsByPeriod(@Argument String period, @Argument int count) {
        log.info("Fetching booking statistics by period: {} for {} periods", period, count);
        return adminDashboardService.getBookingStatisticsByPeriod(period, count);
    }

    /**
     * Get booking counts by status
     */
    @QueryMapping
    public List<BookingStatisticsResponse> getBookingCountsByStatus(@Argument List<String> statuses) {
        log.info("Fetching booking counts by statuses: {}", statuses);
        return adminDashboardService.getBookingCountsByStatus(statuses);
    }

    /**
     * Get revenue statistics
     */
    @QueryMapping
    public RevenueStatisticsResponse getRevenueStatistics(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        log.info("Fetching revenue statistics from {} to {}", startDate, endDate);
        return adminDashboardService.getRevenueStatistics(startDate, endDate);
    }

    /**
     * Get total website revenue
     */
    @QueryMapping
    public Double getTotalWebsiteRevenue() {
        log.info("Fetching total website revenue");
        return adminDashboardService.getTotalWebsiteRevenue();
    }

    /**
     * Get subscription revenue breakdown
     */
    @QueryMapping
    public List<RevenueStatisticsResponse> getSubscriptionRevenue(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        log.info("Fetching subscription revenue from {} to {}", startDate, endDate);
        return adminDashboardService.getSubscriptionRevenue(startDate, endDate);
    }

    /**
     * Get booking revenue breakdown
     */
    @QueryMapping
    public List<RevenueStatisticsResponse> getBookingRevenue(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        log.info("Fetching booking revenue from {} to {}", startDate, endDate);
        return adminDashboardService.getBookingRevenue(startDate, endDate);
    }

    /**
     * Simple test query without authentication - for testing GraphQL setup
     */
    @QueryMapping
    public String testGraphQL() {
        log.info("GraphQL test query called");
        return "GraphQL is working! Current time: " + java.time.LocalDateTime.now();
    }

    /**
     * Get basic booking count without authentication - for testing
     */
    @QueryMapping
    public Long getBasicBookingCount() {
        log.info("Getting basic booking count");
        return bookingRepository.count();
    }

    /**
     * Test admin dashboard without authentication - for testing GraphQL setup
     */
    @QueryMapping
    public AdminDashboardResponse getTestAdminDashboard() {
        log.info("Fetching test admin dashboard data (no auth required)");
        return adminDashboardService.getAdminDashboard();
    }
} 