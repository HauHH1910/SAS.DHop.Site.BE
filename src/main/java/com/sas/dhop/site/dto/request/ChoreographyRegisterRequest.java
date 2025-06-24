package com.sas.dhop.site.dto.request;

import com.sas.dhop.site.model.Area;

import java.util.List;

public record ChoreographyRegisterRequest(List<Integer> danceType, String about, Integer yearExperience) {}
