package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.BookingDetailResponse;
import com.sas.dhop.site.dto.response.BookingStatisticsResponse;
import com.sas.dhop.site.dto.response.OverviewStatisticsResponse;
import com.sas.dhop.site.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@Tag(name = "[Dashboard Controller]")
@Slf4j(topic = "[Dashboard Controller]")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/overview-statistics")
    public ResponseData<OverviewStatisticsResponse> overviewStatistics() {
        return ResponseData.ok(dashboardService.overviewStatistics(), ResponseMessage.GET_DASHBOARD_FOR_ADMIN);
    }

    @GetMapping("/booking-statistics")
    public ResponseData<List<BookingStatisticsResponse>> getBookingStatistics(
            @RequestParam String timeFrame,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime dateTime = (date != null) ? date.atStartOfDay() : null;
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;
        log.info(
                "Fetching booking statistics with timeFrame: {}, date: {}, startDate: {}, endDate: {}",
                timeFrame,
                dateTime,
                startDateTime,
                endDateTime);
        List<BookingStatisticsResponse> statistics =
                dashboardService.getBookingStatistics(timeFrame, dateTime, startDateTime, endDateTime);
        return ResponseData.ok(statistics, ResponseMessage.GET_BOOKING_STATISTICS);
    }

    @GetMapping("/booking")
    public ResponseData<List<BookingDetailResponse>> getBookingDetails(@RequestParam(required = false) String statusName) {
        return ResponseData.ok(
                dashboardService.getBookingDetails(statusName),
                ResponseMessage.GET_BOOKING_DETAILS);
    }
}
