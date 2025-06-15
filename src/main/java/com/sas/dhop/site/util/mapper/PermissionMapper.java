package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.PermissionRequest;
import com.sas.dhop.site.dto.response.PermissionResponse;
import com.sas.dhop.site.model.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permission toPermission(PermissionRequest request);

	PermissionResponse toPermissionResponse(Permission permission);
}
