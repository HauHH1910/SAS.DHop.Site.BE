package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.CreateUserRequest;
import com.sas.dhop.site.dto.request.UpdateUserRequest;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.dto.response.UserResponse;
import com.sas.dhop.site.model.Area;
import com.sas.dhop.site.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(CreateUserRequest request);

    @Mapping(target = "areaResponse", source = "area", qualifiedByName = "mapToAreaResponse")
    UserResponse mapToUserResponse(User user);

    @Named("mapToAreaResponse")
    default AreaResponse mapToAreaResponse(Area area) {
        if (area != null) {
            return new AreaResponse(area.getDistrict(), area.getWard(), area.getCity(), area.getId());
        } else {
            return null;
        }
    }

    @Mapping(target = "roles", ignore = true)
    void mapToUpdateUser(@MappingTarget User user, UpdateUserRequest request);
}
