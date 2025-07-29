package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.DanceType;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ChoreographerMapper {

    @Mapping(target = "danceTypes", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    Choreography mapToChoreography(ChoreographerRequest request);

    @Mapping(target = "danceTypeName", source = "danceTypes", qualifiedByName = "mapDanceTypeToName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "userName", source = "user.name")
    ChoreographerResponse mapToChoreographerResponse(Choreography choreography);

    @Named("mapDanceTypeToName")
    default List<String> mapDanceTypeToName(Set<DanceType> danceTypes) {
        if (danceTypes == null) {
            return List.of();
        }
        return danceTypes.stream().map(DanceType::getType).toList();
    }

    @Mapping(target = "danceTypes", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    void mapToUpdateChoreography(@MappingTarget Choreography choreography, ChoreographerRequest request);
}
