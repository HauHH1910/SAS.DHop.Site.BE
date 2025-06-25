package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.PerformanceStatus.CREATE_PERFORMANCE;

import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.Performance;
import com.sas.dhop.site.repository.PerformanceRepository;
import com.sas.dhop.site.service.PerformanceService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import java.util.List;
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

        if (performance.getBooking() != null) {
            throw new BusinessException(ErrorConstant.PERFORMANCE_CAN_NOT_BE_DELETED);
        }

        performanceRepository.delete(performance);
    }

    @Override
    public void uploadPerformanceForBooking(String url, Booking booking) {
        performanceRepository.save(Performance.builder()
                .status(statusService.findStatusOrCreated(CREATE_PERFORMANCE))
                .user(userService.getLoginUser())
                .mediaUrl(url)
                .booking(booking)
                .build());
    }

    @Override
    public List<PerformanceResponse> getAllPerformanceBelongToCurrentUser() {
        return performanceRepository.findByUser(userService.getLoginUser()).stream()
                .map(PerformanceResponse::mapToPerformance)
                .toList();
    }

    @Override
    public List<PerformanceResponse> getAllPerformanceBelongToBooking(Integer bookingId) {
        return performanceRepository.findByBooking(bookingId).stream()
                .map(PerformanceResponse::mapToPerformance)
                .toList();
    }

    @Override
    public List<String> getPerformanceByBookingId(Integer bookingId) {
        return performanceRepository.findByBooking(bookingId).stream()
                .map(Performance::getMediaUrl)
                .toList();
    }
}
