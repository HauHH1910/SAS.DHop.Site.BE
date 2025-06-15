package com.sas.dhop.site.dto.response;

public record AuthenticationResponse(String accessToken, String refreshToken, UserResponse user) {}
