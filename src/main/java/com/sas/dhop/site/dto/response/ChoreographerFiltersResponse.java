package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ChoreographerFiltersResponse(
        Integer areaId,
        List<Integer> danceTypeId,
        BigDecimal price,
        Set<String> danceTypeName,
        Integer userId,
        String about,
        Integer yearExperience,
        Integer statusId,
        String name) {}
