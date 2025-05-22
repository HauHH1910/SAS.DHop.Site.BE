package com.sas.dhop.site.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OAuthUserResponse(
        String id,
        String email,
        boolean verifiedEmail,
        String name,
        String givenName,
        String familyName,
        String picture,
        String locale) {}
