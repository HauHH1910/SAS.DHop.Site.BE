package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.DancerRequest;
import com.sas.dhop.site.dto.response.DancerResponse;
import com.sas.dhop.site.model.DanceType;
import com.sas.dhop.site.model.Dancer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DancerMapper {
	@Mapping(target = "danceTypes", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "subscription", ignore = true)
	@Mapping(target = "status", ignore = true)
	Dancer mapToDancer(DancerRequest request);

	@Mapping(target = "danceTypeName", source = "danceTypes", qualifiedByName = "mapDanceTypeToId")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "subscriptionId", source = "subscription.id")
	@Mapping(target = "statusId", source = "status.id")
	@Mapping(target = "dancerId", source = "id")
	@Mapping(target = "dancerPhone", source = "user.phone")
	DancerResponse mapToDancerResponse(Dancer dancer);

	@Named("mapDanceTypeToId")
	default Set<String> mapDanceTypeToId(Set<DanceType> danceTypes) {
		if (danceTypes == null) {
			return Collections.emptySet();
		}
		return danceTypes.stream().map(DanceType::getType).collect(Collectors.toSet());
	}

	@Mapping(target = "danceTypes", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "subscription", ignore = true)
	@Mapping(target = "status", ignore = true)
	void mapToUpdateDancer(@MappingTarget Dancer dancer, DancerRequest request);

	default List<Integer> map(Set<DanceType> danceTypes) {
		if (danceTypes == null)
			return new ArrayList<>();
		return danceTypes.stream().map(DanceType::getId).collect(Collectors.toList());
	}
}
