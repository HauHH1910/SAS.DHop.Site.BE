package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.PerformanceStatus.CREATE_PERFORMANCE;

import com.sas.dhop.site.constant.PerformanceStatus;
import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Performance;
import com.sas.dhop.site.repository.PerformanceRepository;
import com.sas.dhop.site.service.PerformanceService;
import com.sas.dhop.site.service.StatusService;
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
    private final StatusService statusService;

    @Override
    public PerformanceResponse createPerformance(PerformanceRequest request) {
        return PerformanceResponse.mapToPerformance(performanceRepository.save(Performance.builder()
                .user(userService.getLoginUser())
                .mediaUrl(request.mediaUrl())
                .status(statusService.findStatusOrCreated(CREATE_PERFORMANCE))
                .build()));
    }

    @Override
    public PerformanceResponse getPerformanceById(Integer id) {
        return PerformanceResponse.mapToPerformance(performanceRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.PERFORMANCE_NOT_FOUND)));
    }

    @Override
    public void deletePerformanceById(Integer id) {
        Performance performance = performanceRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.PERFORMANCE_NOT_FOUND));

        if (PerformanceStatus.PERFORMANCE_BELONG_TO_BOOKING.equals(
                performance.getStatus().getStatusName())) {
            throw new BusinessException(ErrorConstant.PERFORMANCE_CAN_NOT_BE_DELETED);
        }

        performanceRepository.delete(performance);
    }

    @Override
    public void uploadPerformanceForBooking(PerformanceRequest request) {
        performanceRepository.save(Performance.builder()
                .status(statusService.findStatusOrCreated(PerformanceStatus.PERFORMANCE_BELONG_TO_BOOKING))
                .user(userService.getLoginUser())
                .mediaUrl(request.mediaUrl())
                .build());
    }
}
