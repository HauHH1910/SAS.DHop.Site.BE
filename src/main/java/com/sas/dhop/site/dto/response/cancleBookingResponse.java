package com.sas.dhop.site.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;

public record cancleBookingResponse (
    Integer bookingId,
    String reason,
    String cancelPerson,
    LocalDateTime cancelDate
)
{}
