package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.AreaRequest;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.model.Area;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AreaMapper {
	default AreaResponse mapToAreaResponse(Area area) {
		return AreaResponse.mapToAreaResponse(area);
	}

	Area mapToArea(AreaRequest request);
}
