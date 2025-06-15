package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.RoleRequest;
import com.sas.dhop.site.dto.response.RoleResponse;
import com.sas.dhop.site.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  @Mapping(target = "permissions", ignore = true)
  Role mapToRole(RoleRequest request);

  RoleResponse mapToRoleResponse(Role role);
}
