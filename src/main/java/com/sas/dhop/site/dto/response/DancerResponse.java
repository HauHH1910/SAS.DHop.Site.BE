package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Builder;

@Builder
public record DancerResponse(
        String dancerNickName,
        Set<String> danceTypeName,
        Integer userId,
        int yearExperience,
        int teamSize,
        BigDecimal price,
        Integer subscriptionId,
        Integer statusId,
        Integer dancerId,
        Integer areaId,
        String dancerPhone,
        String dancerEmail,
        String about) {}
