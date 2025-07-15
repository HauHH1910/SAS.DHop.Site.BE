package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.*;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {BookingMapper.class, DancerMapper.class, ChoreographerMapper.class})
public interface DashboardMapper {

    OverviewStatisticsResponse toAdminDashboardResponse(
            Long totalUsers, Long totalDancers, Long totalChoreographers, Long totalBookings, BigDecimal totalRevenue);
}
