package com.sas.dhop.site.dto.request;

import com.sas.dhop.site.model.enums.RoleName;

public record RegisterRequest(String email, String name, String password, RoleName role) {}
