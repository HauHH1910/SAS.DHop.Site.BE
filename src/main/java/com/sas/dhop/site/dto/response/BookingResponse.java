package com.sas.dhop.site.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.DanceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime bookingDate,
        String customerPhone,
        Integer numberOfTrainingSessions,
        Integer numberOfTeamMember,
        String choreographyPhone,
        String dancerPhone,
        BigDecimal price,
        List<String> danceTypeName,
        String detail,
        List<String> urls) {

    public static BookingResponse mapToBookingResponse(Booking request, List<String> mediaResponses) {
        return new BookingResponse(
                request.getId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getCustomer().getName(),
                getDancer(request),
                getChoreography(request),
                AreaResponse.mapToAreaResponse(request.getArea()),
                request.getAddress(),
                request.getStatus().getStatusName(),
                request.getBookingDate(),
                request.getCustomerPhone(),
                request.getNumberOfTrainingSessions(),
                request.getNumberOfTeamMember(),
                request.getChoreographyPhone(),
                request.getDancerPhone(),
                request.getPrice(),
                request.getDanceType().stream().map(DanceType::getType).toList(),
                request.getDetail(),
                mediaResponses);
    }

    private static String getChoreography(Booking request) {
        return request.getChoreography() != null && request.getChoreography().getUser() != null
                ? request.getChoreography().getUser().getName()
                : "";
    }

    private static String getDancer(Booking request) {
        return request.getDancer() != null && request.getDancer().getDancerNickName() != null
                ? request.getDancer().getDancerNickName()
                : "";
    }
}
