package com.sas.dhop.site.dto.request;

import java.util.List;

public record DancerRegisterRequest(
    List<Integer> danceType,
    String dancerNickName,
    String about,
    Integer yearExperience,
    Integer teamSize) {}
