package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.AdminDashboardRequest;
import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.dto.response.ChoreographerDashboardResponse;
import com.sas.dhop.site.dto.response.DancerDashboardResponse;
import com.sas.dhop.site.model.Status;

import java.util.List;

public interface DashboardService {
    List<AdminDashboardResponse> getAdminDashboard(String statusName);

    DancerDashboardResponse getDancerDashboard(DancerDashboardRequest request);

    ChoreographerDashboardResponse getChoreographerDashboard(ChoreographerDashboardRequest request);
}
