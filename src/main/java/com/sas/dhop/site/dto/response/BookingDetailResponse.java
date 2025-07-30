package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.BookingFeedback;
import com.sas.dhop.site.model.DanceType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

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
    private BigDecimal price;
    private BigDecimal commissionPrice;
    private Integer rating;

    public static BookingDetailResponse mapToBookingDetail(Booking booking, BookingFeedback bookingFeedback) {
        return BookingDetailResponse.builder()
                .bookingId(booking.getId())
                .customerName(booking.getCustomer().getName())
                .dancerName(booking.getDancer() != null ? booking.getDancer().getDancerNickName() : null)
                .choreographyName(
                        booking.getChoreography() != null
                                ? booking.getChoreography().getUser().getName()
                                : null)
                .areaName(booking.getArea().getCity())
                .statusName(booking.getStatus().getStatusName())
                .bookingDate(booking.getBookingDate().toString())
                .startTime(booking.getStartTime().toString())
                .endTime(booking.getEndTime().toString())
                .address(booking.getAddress())
                .description(booking.getDetail())
                .price(booking.getPrice())
                .danceTypes(
                        booking.getDanceType().stream().map(DanceType::getType).toList())
                .commissionPrice(calculateCommissionPrice(booking.getPrice()))
                .rating(bookingFeedback != null ? bookingFeedback.getRating() : null)
                .build();
    }

    private static BigDecimal calculateCommissionPrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("500000")) >= 0 && price.compareTo(new BigDecimal("1000000")) < 0) {
            // Phí hoa hồng 20%
            return price.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP);
        } else if (price.compareTo(new BigDecimal("1000000")) >= 0 && price.compareTo(new BigDecimal("2000000")) < 0) {
            // Phí hoa hồng 15%
            return price.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Phí hoa hồng 10% (>= 2 triệu)
            return price.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
