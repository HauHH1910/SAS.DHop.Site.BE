package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ChoreographerResponse(
    String about,
    int yearExperience,
    Integer userId,
    BigDecimal price,
    List<Integer> danceTypeId,
    Integer subscriptionId,
    Integer statusId,
    String userName) {}
