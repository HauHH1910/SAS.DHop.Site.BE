package com.sas.dhop.site.dto.request;

import static com.sas.dhop.site.exception.MessageKey.DANCER_END_TIME_NOT_NULL;
import static com.sas.dhop.site.exception.MessageKey.DANCER_START_TIME_NOT_NULL;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DancerBookingRequest(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") @NotNull(message = DANCER_START_TIME_NOT_NULL)
                LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") @NotNull(message = DANCER_END_TIME_NOT_NULL) LocalDateTime endTime,
        String address,
        String detail,
        Integer dancerId,
        Integer areaId,
        String danceTypeName,
        BigDecimal bookingPrice,
        String dancerPhone,
        String customerPhone,
        Integer teamSize) {}
