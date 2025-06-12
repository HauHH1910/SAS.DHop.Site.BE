package com.sas.dhop.site.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.sas.dhop.site.exception.MessageKey.DANCER_END_TIME_NOT_NULL;
import static com.sas.dhop.site.exception.MessageKey.DANCER_START_TIME_NOT_NULL;

public record BookingRequest(
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") @NotNull(message = DANCER_START_TIME_NOT_NULL) LocalDateTime startTime,
        // Start time of the booking
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") @NotNull(message = DANCER_END_TIME_NOT_NULL) LocalDateTime endTime,
        // End time of the booking
        String address,
        String detail,
        Integer dancerId,
        Integer areaId,
        Integer choreographyId,
        Integer danceTypeId,
        String customerPhone,
        Long price,
        Integer numberOfTrainingSessions) {}
        BigDecimal bookingPrice,
        String customerPhone) {}
