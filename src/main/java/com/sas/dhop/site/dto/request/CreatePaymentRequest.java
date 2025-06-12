package com.sas.dhop.site.dto.request;

public record CreatePaymentRequest(
    String name,
    String description,
    String returnUrl,
    Integer price,
    String cancelUrl
) {
}
