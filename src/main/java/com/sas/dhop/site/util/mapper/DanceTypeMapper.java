package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.DanceTypeRequest;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.model.DanceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DanceTypeMapper {

	DanceType mapToModel(DanceTypeRequest request);

	DanceTypeResponse mapToResponse(DanceType danceType);
}
