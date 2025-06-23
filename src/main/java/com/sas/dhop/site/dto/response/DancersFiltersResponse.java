package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record DancersFiltersResponse(
        Integer areaId,
        List<Integer> danceTypeId,
        BigDecimal price,
        int teamSize,
        String dancerNickName,
        Set<String> danceTypeName,
        Integer userId,
        int yearExperience,
//        Integer subscriptionId,
        Integer statusId,
        Integer dancerId,
        String dancerPhone

) {
}
