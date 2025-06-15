package com.sas.dhop.site.dto.response;

import java.time.LocalDateTime;

public record CancelBookingResponse(Integer bookingId, String reason, String cancelPerson, LocalDateTime cancelDate) {
}
