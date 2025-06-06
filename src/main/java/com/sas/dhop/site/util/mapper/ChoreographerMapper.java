package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.DanceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChoreographerMapper {
    
    @Mapping(target = "danceTypes", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "status", ignore = true)
    Choreography mapToChoreography(ChoreographerRequest request);

    @Mapping(target = "danceTypeId", source = "danceTypes", qualifiedByName = "mapDanceTypeToId")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "subscriptionId", source = "subscription.id")
    @Mapping(target = "statusId", source = "status.id")
    ChoreographerResponse mapToChoreographerResponse(Choreography choreography);

    @Named("mapDanceTypeToId")
    default List<Integer> mapDanceTypeToId(Set<DanceType> danceTypes) {
        if (danceTypes == null) {
            return List.of();
        }
        return danceTypes.stream().map(DanceType::getId).collect(Collectors.toList());
    }

    @Mapping(target = "danceTypes", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "status", ignore = true)
    void mapToUpdateChoreography(@MappingTarget Choreography choreography, ChoreographerRequest request);
}
