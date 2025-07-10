package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.OverviewStatisticsResponse;
import com.sas.dhop.site.dto.response.ChoreographerDashboardResponse;
import com.sas.dhop.site.dto.response.DancerDashboardResponse;

import java.util.List;

public interface DashboardService {
    OverviewStatisticsResponse overviewStatistics();

    List<Object> revenueAndUserGrowth();

    DancerDashboardResponse getDancerDashboard(DancerDashboardRequest request);

    ChoreographerDashboardResponse getChoreographerDashboard(ChoreographerDashboardRequest request);
}
