package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.enums.RoleName;
import java.util.Set;

public record RoleResponse(RoleName name, Set<PermissionResponse> permissions) {}
