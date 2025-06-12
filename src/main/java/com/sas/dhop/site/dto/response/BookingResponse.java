package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record BookingResponse(
        Instant bookingDate,
        @JsonFormat(pattern = "dd/MM/yyyy HH:ss")
        LocalTime startDay,

        @JsonFormat(pattern = "dd/MM/yyyy HH:ss")
        LocalTime endTime,
        String address,
        String detail,
        LocalDateTime updateBookingDate,
        String bookingStatus,
        String customerPhone,
        BigDecimal bookingPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
