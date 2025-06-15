package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.CreateUserRequest;
import com.sas.dhop.site.dto.request.UpdateUserRequest;
import com.sas.dhop.site.dto.response.UserResponse;
import com.sas.dhop.site.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

	User mapToUser(CreateUserRequest request);

	UserResponse mapToUserResponse(User user);

	@Mapping(target = "roles", ignore = true)
	void mapToUpdateUser(@MappingTarget User user, UpdateUserRequest request);
}
