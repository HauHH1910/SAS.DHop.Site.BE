package com.sas.dhop.site.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingRequest(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
        String address,
        String detail,
        Integer dancerId,
        Integer areaId,
        Integer choreographyId,
        String danceTypeName,
        Integer numberOfTrainingSessions,
        BigDecimal bookingPrice,
        String dancerPhone,
        String choreographyPhone,
        String customerPhone) {}
