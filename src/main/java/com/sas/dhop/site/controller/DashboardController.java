package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Dashboard")
@Tag(name = "[Dashboard Controller]")
@Slf4j(topic = "[Dashboard Controller]")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/get-dashboard-for-admin")
    public ResponseData<List<AdminDashboardResponse>> getAllForAdmin(@RequestParam(required = false)String statusName){
        return ResponseData.<List<AdminDashboardResponse>>builder()
                .message(ResponseMessage.GET_DASHBOARD_FOR_ADMIN)
                .data(dashboardService.getAdminDashboard(statusName))
                .build();
    }

}
