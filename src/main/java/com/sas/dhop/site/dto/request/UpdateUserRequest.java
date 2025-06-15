package com.sas.dhop.site.dto.request;

public record UpdateUserRequest(
    String avatar, Integer age, String name, String email, String phone) {}
