package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.model.Performance;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.repository.PerformanceRepository;
import com.sas.dhop.site.service.PerformanceService;
import com.sas.dhop.site.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Performance Service]")
public class PerformanceServiceImpl implements PerformanceService {
	private final UserService userService;
	private final PerformanceRepository performanceRepository;

	@Override
	public PerformanceResponse createPerformance(PerformanceRequest request) {
		User loginUser = userService.getLoginUser();

		Performance performance = performanceRepository
				.save(Performance.builder().user(loginUser).mediaUrl(request.mediaUrl()).build());
		return PerformanceResponse.mapToPerformance(performance);
	}

	@Override
	public PerformanceResponse getPerformanceById(Integer id) {
		return null;
	}

	@Override
	public void deletePerformanceById(Integer id) {
	}
}
