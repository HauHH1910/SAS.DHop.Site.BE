package com.sas.dhop.site.dto.request;

import java.time.LocalDateTime;

public record CreatePaymentRequest(
        String name,
        String description,
        Integer price,
        LocalDateTime bookingDate) {
}
