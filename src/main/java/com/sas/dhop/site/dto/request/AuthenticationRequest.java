package com.sas.dhop.site.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull(message = "EMAIL_NOT_NULL")
                @NotBlank(message = "EMAIL_NOT_BLANK")
                @NotEmpty(message = "EMAIL_NOT_EMPTY")
                String email,
        @NotBlank(message = "PASSWORD_NOT_BLANK")
                @NotEmpty(message = "PASSWORD_NOT_EMPTY")
                @NotNull(message = "PASSWORD_NOT_NULL")
                String password) {}
