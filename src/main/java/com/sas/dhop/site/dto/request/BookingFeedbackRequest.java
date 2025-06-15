package com.sas.dhop.site.dto.request;

public record BookingFeedbackRequest(Integer rating, String comment, Integer bookingId) {
}
