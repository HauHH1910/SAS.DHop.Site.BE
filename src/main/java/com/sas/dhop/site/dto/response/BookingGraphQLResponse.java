package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Booking;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record BookingGraphQLResponse(
        Integer id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String customer,
        String dancer,
        String choreography,
        AreaResponse area,
        String address,
        String status,
        Instant bookingDate,
        String detail,
        Instant updateBookingDate,
        String bookingStatus,
        String customerPhone,
        Integer numberOfTrainingSessions,
        String choreographyPhone,
        String dancerPhone,
        String cancelReason,
        String cancelPersonName,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static BookingGraphQLResponse mapToBookingGraphQLResponse(Booking booking) {
        return BookingGraphQLResponse.builder()
                .id(booking.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .customer(booking.getCustomer().getName())
                .dancer(getDancer(booking))
                .choreography(getChoreography(booking))
                .area(AreaResponse.mapToAreaResponse(booking.getArea()))
                .address(booking.getAddress())
                .status(booking.getStatus().getStatusName())
                .bookingDate(booking.getBookingDate())
                .detail(booking.getDetail())
                .updateBookingDate(booking.getUpdateBookingDate())
                .bookingStatus(booking.getBookingStatus())
                .customerPhone(booking.getCustomerPhone())
                .numberOfTrainingSessions(booking.getNumberOfTrainingSessions())
                .choreographyPhone(booking.getChoreographyPhone())
                .dancerPhone(booking.getDancerPhone())
                .cancelReason(booking.getCancelReason())
                .cancelPersonName(booking.getCancelPersonName())
                .price(booking.getPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    private static String getChoreography(Booking booking) {
        return booking.getChoreography() != null && booking.getChoreography().getUser() != null
                ? booking.getChoreography().getUser().getName()
                : "";
    }

    private static String getDancer(Booking booking) {
        return booking.getDancer() != null && booking.getDancer().getUser().getName() != null
                ? booking.getDancer().getUser().getName()
                : "";
    }
} 