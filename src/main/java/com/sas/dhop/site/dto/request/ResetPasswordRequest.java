package com.sas.dhop.site.dto.request;

public record ResetPasswordRequest(String newPassword, String token) {}
