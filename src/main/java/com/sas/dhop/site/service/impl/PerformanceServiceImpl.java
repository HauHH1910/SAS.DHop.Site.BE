package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Performance;
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
    public void createPerformance(PerformanceRequest request) {
        performanceRepository
                .save(Performance.builder().user(userService.getLoginUser()).mediaUrl(request.mediaUrl()).build());
    }

    @Override
    public PerformanceResponse getPerformanceById(Integer id) {
        return PerformanceResponse.mapToPerformance(performanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.PERFORMANCE_NOT_FOUND)));
    }

    @Override
    public void deletePerformanceById(Integer id) {
        performanceRepository.delete(performanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.PERFORMANCE_NOT_FOUND)));
    }
}
