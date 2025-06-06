package com.sas.dhop.site.dto.request;

import java.time.LocalTime;

public record BookingRequest(
        LocalTime startDay, // Start Job of CH0r
        LocalTime endTime, // Dancer show, Finish job of Choreography
        String address,
        String detail,
        Integer dancerId,
        Integer choreographyId,
        Integer danceTypeId,
        String customerPhone) {}
