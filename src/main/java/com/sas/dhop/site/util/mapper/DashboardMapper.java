package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.model.*;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {BookingMapper.class, DancerMapper.class, ChoreographerMapper.class})
public interface DashboardMapper {
    //    @Mapping(target = "totalUsers", source = "totalUsers")
    //    @Mapping(target = "totalDancers", source = "totalDancers")
    //    @Mapping(target = "totalChoreographers", source = "totalChoreographers")
    //    @Mapping(target = "totalBookings", source = "totalBookings")
    //    @Mapping(target = "totalRevenue", source = "totalRevenue")
    //    @Mapping(target = "recentBookings", source = "recentBookings")
    //    @Mapping(target = "topDancers", source = "topDancers")
    //    @Mapping(target = "topChoreographers", source = "topChoreographers")
    //    AdminDashboardResponse toAdminDashboardResponse(
    //        Long totalUsers,
    //        Long totalDancers,
    //        Long totalChoreographers,
    //        Long totalBookings,
    //        BigDecimal totalRevenue,
    //        List<Booking> recentBookings,
    //        List<Dancer> topDancers,
    //        List<Choreographer> topChoreographers
    //    );
    //
    //    @Mapping(target = "totalBookings", source = "totalBookings")
    //    @Mapping(target = "completedBookings", source = "completedBookings")
    //    @Mapping(target = "pendingBookings", source = "pendingBookings")
    //    @Mapping(target = "totalEarnings", source = "totalEarnings")
    //    @Mapping(target = "upcomingBookings", source = "upcomingBookings")
    //    @Mapping(target = "recentFeedbacks", source = "recentFeedbacks")
    ////    @Mapping(target = "currentSubscription", source = "currentSubscription")
    //    DancerDashboardResponse toDancerDashboardResponse(
    //        Long totalBookings,
    //        Long completedBookings,
    //        Long pendingBookings,
    //        BigDecimal totalEarnings,
    //        List<Booking> upcomingBookings,
    //        List<BookingFeedback> recentFeedbacks
    ////        UserSubscription currentSubscription
    //    );
    //
    //    @Mapping(target = "totalBookings", source = "totalBookings")
    //    @Mapping(target = "completedBookings", source = "completedBookings")
    //    @Mapping(target = "pendingBookings", source = "pendingBookings")
    //    @Mapping(target = "totalEarnings", source = "totalEarnings")
    //    @Mapping(target = "upcomingBookings", source = "upcomingBookings")
    //    @Mapping(target = "recentFeedbacks", source = "recentFeedbacks")
    ////    @Mapping(target = "currentSubscription", source = "currentSubscription")
    //    @Mapping(target = "topDancers", source = "topDancers")
    //    ChoreographerDashboardResponse toChoreographerDashboardResponse(
    //        Long totalBookings,
    //        Long completedBookings,
    //        Long pendingBookings,
    //        BigDecimal totalEarnings,
    //        List<Booking> upcomingBookings,
    //        List<BookingFeedback> recentFeedbacks,
    ////        UserSubscription currentSubscription,
    //        List<Dancer> topDancers
    //    );
}
