package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.service.PerformanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
@Tag(name = "[Performance Controller]")
@Slf4j(topic = "[Performance Controller]")
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping
    public ResponseData<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest request) {
        return ResponseData.<PerformanceResponse>builder()
                .message(ResponseMessage.CREATE_PERFORMANCE)
                .data(performanceService.createPerformance(request))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<PerformanceResponse> getPerformance(@PathVariable("id") Integer id) {
        return ResponseData.<PerformanceResponse>builder()
                .message(ResponseMessage.GET_PERFORMANCE)
                .data(performanceService.getPerformanceById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deletePerformance(@PathVariable("id") Integer id){
        performanceService.deletePerformanceById(id);
        return ResponseData.<Void>builder()
                .message(ResponseMessage.DELETE_PERFORMANCE)
                .build();
    }

    @GetMapping("/current-user")
    public ResponseData<List<PerformanceResponse>> getAllPerformance() {
        return ResponseData.<List<PerformanceResponse>>builder()
                .message(ResponseMessage.GET_ALL_PERFORMANCE_BELONG_TO_CURRENT_USER)
                .data(performanceService.getAllPerformanceBelongToCurrentUser())
                .build();
    }

    @GetMapping("/booking/{id}")
    public ResponseData<List<PerformanceResponse>> getAllPerformanceBelongToBooking(@PathVariable("id") Integer id) {
        return ResponseData.<List<PerformanceResponse>>builder()
                .message(ResponseMessage.GET_ALL_PERFORMANCE_BELONG_TO_BOOKING)
                .data(performanceService.getAllPerformanceBelongToBooking(id))
                .build();
    }

}
