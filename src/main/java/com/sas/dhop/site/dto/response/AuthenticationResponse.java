package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Role;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.enums.RoleName;

import java.util.Set;
import java.util.stream.Collectors;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        UserResponse user,
        Set<RoleName> role,
        DancerResponse dancerResponse
) {

}
