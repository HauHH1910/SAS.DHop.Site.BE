package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;

public interface PerformanceService {

	void createPerformance(PerformanceRequest request);

	PerformanceResponse getPerformanceById(Integer id);

	void deletePerformanceById(Integer id);
}
