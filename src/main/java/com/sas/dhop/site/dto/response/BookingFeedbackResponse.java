package com.sas.dhop.site.dto.response;

public record BookingFeedbackResponse(
        Integer feedbackId,
        Integer fromUserId,
        Integer toUserId,
        Integer rating,
        String comment,
        Integer statusId,
        Integer bookingId) {}
