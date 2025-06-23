package com.sas.dhop.site.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;


@Builder
public record DancersFiltersRequest(
        Integer areaId,
        List<Integer> danceTypeId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer teamSize

) {
}
