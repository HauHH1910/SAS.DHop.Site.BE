package com.sas.dhop.site.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingStatisticsResponse {
        private final String label;              // e.g., "Week 1", "January 2024", "BOOKING_PENDING"
        private final Long count;                // Number of bookings
        private final LocalDate dateFrom;        // Starting date for the period
        private final LocalDate dateTo;          // Ending date for the period
        private final String period;             // "DAILY", "WEEKLY", "MONTHLY", "YEARLY", "STATUS"
        private final String status;             // Booking status (if applicable)
        private final Double percentage;         // Percentage of total (if applicable)
        private final Long previousCount;        // Previous period count for comparison
        private final Double growthRate;          // Growth rate compared to previous period
} 