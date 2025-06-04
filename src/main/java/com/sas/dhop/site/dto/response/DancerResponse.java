package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;

// Remember to check again
public record DancerResponse(
        String dancerNickName,
        List<Integer> danceTypeId,
        Integer userId,
        int yearExperience,
        int teamSize,
        BigDecimal price,
        Integer subscriptionId,
        Integer statusId) {}
