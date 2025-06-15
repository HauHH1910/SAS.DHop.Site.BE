package com.sas.dhop.site.dto.request;

import java.math.BigDecimal;
import java.util.List;

// Remember to check again
public record DancerRequest(String dancerNickName, int yearExperience, int teamSize, BigDecimal price,
		List<Integer> danceTypeId) {
}
