package com.sas.dhop.site.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingFeedbackRequest(
        @NotNull(message = "Rating không được để trống") @Min(value = 1, message = "Rating tối thiểu là 1 sao") @Max(value = 5, message = "Rating tối đa là 5 sao") Integer rating,
        String comment,
        Integer bookingId) {}
