package com.sas.dhop.site.dto.request;

import com.sas.dhop.site.model.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull(message = "EMAIL_NOT_NULL")
        @NotBlank(message = "EMAIL_NOT_BLANK")
        String email,

        @NotBlank(message = "NAME_NOT_BLANK")
        @NotEmpty(message = "NAME_NOT_EMPTY")
        String name,
        String password,
        RoleName role,
        ChoreographyRegisterRequest choreography,
        DancerRegisterRequest dancer) {
}
