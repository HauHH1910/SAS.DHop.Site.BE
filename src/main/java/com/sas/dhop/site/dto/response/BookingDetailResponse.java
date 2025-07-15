package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.DanceType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookingDetailResponse {
    private Integer bookingId;
    private String customerName;
    private String dancerName;
    private String choreographyName;
    private List<String> danceTypes;
    private String areaName;
    private String statusName;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String address;
    private String description;

    public static BookingDetailResponse mapToBookingDetail(Booking booking) {
        return BookingDetailResponse.builder()
                .bookingId(booking.getId())
                .customerName(booking.getCustomer().getName())
                .dancerName(booking.getDancer() != null ? booking.getDancer()
                        .getDancerNickName() : null)
                .choreographyName(booking.getChoreography() != null ? booking.getChoreography()
                        .getUser().getName() : null)
                .areaName(booking.getArea().getCity())
                .statusName(booking.getStatus().getStatusName())
                .bookingDate(booking.getBookingDate().toString())
                .startTime(booking.getStartTime().toString())
                .endTime(booking.getEndTime().toString())
                .address(booking.getAddress())
                .description(booking.getDetail())
                .danceTypes(booking.getDanceType().stream()
                        .map(DanceType::getType).toList())
                .build();
    }
}
