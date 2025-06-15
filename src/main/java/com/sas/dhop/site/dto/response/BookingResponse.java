package com.sas.dhop.site.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sas.dhop.site.model.Booking;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
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
    Instant bookingDate,
    String customerPhone,
    Integer numberOfTrainingSessions,
    String choreographyPhone,
    String dancerPhone,
    BigDecimal price) {

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
        .bookingDate(request.getBookingDate())
        .customerPhone(request.getCustomerPhone())
        .numberOfTrainingSessions(request.getNumberOfTrainingSessions())
        .choreographyPhone(request.getChoreographyPhone())
        .dancerPhone(request.getDancerPhone())
        .price(request.getPrice())
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
