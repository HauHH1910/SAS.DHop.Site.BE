package com.sas.dhop.site.dto.request;

public record ResetPasswordRequest(String email, String newPassword, String token) {}
