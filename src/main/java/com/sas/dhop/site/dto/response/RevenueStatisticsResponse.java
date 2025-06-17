package com.sas.dhop.site.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RevenueStatisticsResponse {
        private final String label;                    // e.g., "Booking Revenue", "Subscription Revenue", "January 2024"
        private final BigDecimal amount;               // Revenue amount
        private final String currency;                 // Currency code (e.g., "VND", "USD")
        private final LocalDate dateFrom;              // Starting date for the period
        private final LocalDate dateTo;                // Ending date for the period
        private final String revenueType;              // "BOOKING", "SUBSCRIPTION", "TOTAL", "OTHER"
        private final String period;                   // "DAILY", "WEEKLY", "MONTHLY", "YEARLY"
        private final Long transactionCount;           // Number of transactions contributing to this revenue
        private final BigDecimal averageTransactionValue; // Average transaction value
        private final Double percentage;               // Percentage of total revenue
        private final BigDecimal previousAmount;       // Previous period amount for comparison
        private final Double growthRate;               // Growth rate compared to previous period
        private final String description;               // Additional description
} 