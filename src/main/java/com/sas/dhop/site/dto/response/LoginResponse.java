package com.sas.dhop.site.dto.response;

public record LoginResponse(boolean authenticated, String token) {}
