package com.sas.dhop.site.dto.request;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record BookingRequest(LocalDateTime bookingDate, LocalTime startDay, LocalTime endTime, String address, String detail,  String bookingStatus, String customerPhone) {}
