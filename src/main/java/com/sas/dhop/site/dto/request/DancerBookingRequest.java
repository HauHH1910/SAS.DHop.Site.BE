package com.sas.dhop.site.dto.request;

import static com.sas.dhop.site.exception.MessageKey.DANCER_END_TIME_NOT_NULL;
import static com.sas.dhop.site.exception.MessageKey.DANCER_START_TIME_NOT_NULL;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record DancerBookingRequest
        (
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
                Date bookingDate, //cái ngày đi diễn
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
                @NotNull(message = DANCER_END_TIME_NOT_NULL)
                LocalDateTime endTime,
                String address,
                String detail,
                Integer dancerId,
                Integer areaId,
                List<String> danceTypeName,
                BigDecimal bookingPrice,
                String dancerPhone,
                String customerPhone,
                Integer numberOfTeamMember
        ) {
}
