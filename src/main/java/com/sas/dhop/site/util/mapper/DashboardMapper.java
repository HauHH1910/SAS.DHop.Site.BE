package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.*;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        uses = {BookingMapper.class, DancerMapper.class, ChoreographerMapper.class}
)
public interface DashboardMapper {


     AdminDashboardResponse toAdminDashboardResponse(
             Long totalUsers,
             Long totalDancers,
             Long totalChoreographers,
             Long totalBookings,
             BigDecimal totalRevenue
     );
}
