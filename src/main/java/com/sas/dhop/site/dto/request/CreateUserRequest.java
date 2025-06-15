package com.sas.dhop.site.dto.request;

public record CreateUserRequest(String email, String password, String phone, String name) {
}
