package com.sas.dhop.site.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record BookingResponse(
        Instant bookingDate,
        LocalTime startDay,
        LocalTime endTime,
        String address,
        String detail,
        LocalDateTime updateBookibgDate,
        String bookingStatus,
        String customerPhone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
