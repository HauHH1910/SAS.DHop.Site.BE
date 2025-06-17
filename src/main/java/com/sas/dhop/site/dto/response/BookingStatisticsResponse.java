package com.sas.dhop.site.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookingStatisticsResponse(
        String label,              // e.g., "Week 1", "January 2024", "BOOKING_PENDING"
        Long count,                // Number of bookings
        LocalDate dateFrom,        // Starting date for the period
        LocalDate dateTo,          // Ending date for the period
        String period,             // "DAILY", "WEEKLY", "MONTHLY", "YEARLY", "STATUS"
        String status,             // Booking status (if applicable)
        Double percentage,         // Percentage of total (if applicable)
        Long previousCount,        // Previous period count for comparison
        Double growthRate          // Growth rate compared to previous period
) {} 