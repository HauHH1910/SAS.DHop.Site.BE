package com.sas.dhop.site.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record DancersFiltersRequest(
        Integer areaId,
        List<Integer> danceTypeId,
        BigDecimal price,
        int teamSize

) {
}
