package com.sas.dhop.site.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sas.dhop.site.model.Booking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.sas.dhop.site.model.DanceType;
import lombok.Builder;

// yyyy-MM-dd HH:mm
// Title, start, end, description, id[string||int], people []
@Builder
public record BookingResponse(
        Integer id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime start,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime end,
        String customer,
        String dancer,
        String choreography,
        AreaResponse area,
        String address,
        String status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime bookingDate,
        String customerPhone,
        Integer numberOfTrainingSessions,
        Integer numberOfTeamMember,
        String choreographyPhone,
        String dancerPhone,
        BigDecimal price,
        List<String> danceTypeName,
        String detail) {

    public static BookingResponse mapToBookingResponse(Booking request) {
        return BookingResponse.builder()
                .id(request.getId())
                .start(request.getStartTime())
                .end(request.getEndTime())
                .customer(request.getCustomer().getName())
                .dancer(getDancer(request))
                .choreography(getChoreography(request))
                .area(AreaResponse.mapToAreaResponse(request.getArea()))
                .address(request.getAddress())
                .status(request.getStatus().getStatusName())
                .detail(request.getDetail())
                .bookingDate(request.getBookingDate())
                .customerPhone(request.getCustomerPhone())
                .numberOfTrainingSessions(request.getNumberOfTrainingSessions())
                .choreographyPhone(request.getChoreographyPhone())
                .numberOfTeamMember(request.getNumberOfTeamMember())
                .dancerPhone(request.getDancerPhone())
                .price(request.getPrice())
                .danceTypeName(request.getDanceType().stream().map(DanceType::getType).toList())
                .build();
    }

    private static String getChoreography(Booking request) {
        return request.getChoreography() != null && request.getChoreography().getUser() != null
                ? request.getChoreography().getUser().getName()
                : "";
    }

    private static String getDancer(Booking request) {
        return request.getDancer() != null && request.getDancer().getUser().getName() != null
                ? request.getDancer().getUser().getName()
                : "";
    }
}
