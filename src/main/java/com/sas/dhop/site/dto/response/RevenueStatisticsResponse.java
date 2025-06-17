package com.sas.dhop.site.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record RevenueStatisticsResponse(
        String label,                    // e.g., "Booking Revenue", "Subscription Revenue", "January 2024"
        BigDecimal amount,               // Revenue amount
        String currency,                 // Currency code (e.g., "VND", "USD")
        LocalDate dateFrom,              // Starting date for the period
        LocalDate dateTo,                // Ending date for the period
        String revenueType,              // "BOOKING", "SUBSCRIPTION", "TOTAL", "OTHER"
        String period,                   // "DAILY", "WEEKLY", "MONTHLY", "YEARLY"
        Long transactionCount,           // Number of transactions contributing to this revenue
        BigDecimal averageTransactionValue, // Average transaction value
        Double percentage,               // Percentage of total revenue
        BigDecimal previousAmount,       // Previous period amount for comparison
        Double growthRate,               // Growth rate compared to previous period
        String description               // Additional description
) {} 