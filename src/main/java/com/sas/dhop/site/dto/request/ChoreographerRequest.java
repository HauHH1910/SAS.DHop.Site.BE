package com.sas.dhop.site.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record ChoreographerRequest(String about, int yearExperience, BigDecimal price, List<Integer> danceTypeId) {}
