package com.sas.dhop.site.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record BookingRequest(
        @JsonFormat(pattern = "dd/MM/yy HH:mm") @NotNull(message = "Start time is required") LocalDateTime startTime, // Start time of the booking
        @JsonFormat(pattern = "dd/MM/yy HH:mm") @NotNull(message = "End time is required") LocalDateTime endTime, // End time of the booking
        String address,
        String detail,
        Integer dancerId,
        Integer choreographyId,
        Integer danceTypeId,
        String customerPhone) {}
