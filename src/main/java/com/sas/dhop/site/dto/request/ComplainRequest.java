package com.sas.dhop.site.dto.request;

public record ComplainRequest(
        String content,
        Integer bookingId) {
}
