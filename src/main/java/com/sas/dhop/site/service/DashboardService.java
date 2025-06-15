package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.AdminDashboardRequest;
import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.dto.response.ChoreographerDashboardResponse;
import com.sas.dhop.site.dto.response.DancerDashboardResponse;

public interface DashboardService {
  AdminDashboardResponse getAdminDashboard(AdminDashboardRequest request);

  DancerDashboardResponse getDancerDashboard(DancerDashboardRequest request);

  ChoreographerDashboardResponse getChoreographerDashboard(ChoreographerDashboardRequest request);
}
