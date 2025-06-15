package com.sas.dhop.site.dto.request;

import com.sas.dhop.site.model.enums.RoleName;
import java.util.Set;

public record RoleRequest(RoleName name, Set<String> permissions) {
}
