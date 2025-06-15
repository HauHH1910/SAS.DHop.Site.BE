package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Area;

public record AreaResponse(String district, String ward, String city, Integer areaId) {

	public static AreaResponse mapToAreaResponse(Area area) {
		return new AreaResponse(area.getDistrict(), area.getWard(), area.getCity(), area.getId());
	}
}
