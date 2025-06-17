package com.sas.dhop.site.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.sas.dhop.site.exception.ErrorConstant.NUMBER_OF_TEAM_MEMBER_MIN_VALUE;

public record DancerBookingRequest(
        //Validate booking date
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @NotNull(message = "BOOKING_DATE_NOT_NULL")
        LocalDateTime bookingDate, // cái ngày đi diễn

        //Validate end time
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @NotNull(message = "END_TIME_NOT_NULL")
        LocalDateTime endTime,
        //Validate address
        @NotNull(message = "ADDRESS_NOT_NULL")
        @NotBlank(message = "ADDRESS_NOT_BLANK")
        String address,
        //Validate detail
        @NotNull(message = "DETAIL_NOT_NULL")
        String detail,
        //Validate dancer
        @NotNull(message = "DANCER_NOT_NULL")
        Integer dancerId,

        //Validate area
        @NotNull(message = "AREA_NOT_NULL")
        Integer areaId,
        //Validate dance type name
        @NotNull(message = "DANCE_TYPE_NAME_NOT_NULL")
        List<String> danceTypeName,
        //Validate price
        @NotNull(message = "PRICE_NOT_NULL")
        BigDecimal bookingPrice,
        //Validate dancer phone
        @NotBlank(message = "DANCER_PHONE_NOT_BLANK")
        @NotNull(message = "DANCER_PHONE_NOT_NULL")
        String dancerPhone,

        //Validate customer phone
        @NotBlank(message = "CUSTOMER_PHONE_NOT_BLANK")
        @NotNull(message = "CUSTOMER_PHONE_NOT_NULL")
        String customerPhone,

        //Validate number of team member
        @NotNull(message = "NUMBER_OF_TEAM_MEMBER_NOT_NULL")
        @Min(message = "NUMBER_OF_TEAM_MEMBER_MIN_VALUE", value = 1)
        Integer numberOfTeamMember
) {
}
