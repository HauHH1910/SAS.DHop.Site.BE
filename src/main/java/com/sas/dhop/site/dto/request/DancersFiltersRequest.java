package com.sas.dhop.site.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record DancersFiltersRequest(
        Integer areaId, List<Integer> danceTypeId, BigDecimal minPrice, BigDecimal maxPrice, Integer teamSize) {}
