package com.sas.dhop.site.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ChoreographerFiltersRequest(
        Integer areaId,
        List<Integer> danceTypeId,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
