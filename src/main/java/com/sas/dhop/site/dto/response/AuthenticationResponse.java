package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.enums.RoleName;
import java.util.Set;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        UserResponse user,
        Set<RoleName> role,
        DancerResponse dancerResponse) {}
