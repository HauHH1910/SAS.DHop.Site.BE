package com.sas.dhop.site.dto.request;

public record VerifyOTPRequest(String email, String otpCode) {
}
