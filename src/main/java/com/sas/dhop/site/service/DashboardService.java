package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.BookingStatisticsResponse;
import com.sas.dhop.site.dto.response.OverviewStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {
    OverviewStatisticsResponse overviewStatistics();

    List<BookingStatisticsResponse> getBookingStatistics(
            String timeFrame, LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate);
}
