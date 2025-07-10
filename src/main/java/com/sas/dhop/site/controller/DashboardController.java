package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.OverviewStatisticsResponse;
import com.sas.dhop.site.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
