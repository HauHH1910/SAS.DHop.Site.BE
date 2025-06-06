package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.Set;

public record DancerResponse(
        String dancerNickName,
        Set<String> danceTypeName,
        Integer userId,
        int yearExperience,
        int teamSize,
        BigDecimal price,
        Integer subscriptionId,
        Integer statusId) {}
