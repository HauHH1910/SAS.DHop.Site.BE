package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.PerformanceRequest;
import com.sas.dhop.site.dto.response.PerformanceResponse;
import com.sas.dhop.site.model.Booking;

import java.util.List;

public interface PerformanceService {

    List<String> getPerformanceByBookingId(Integer bookingId);

    PerformanceResponse createPerformance(PerformanceRequest request);

    PerformanceResponse getPerformanceById(Integer id);

    void deletePerformanceById(Integer id);

    void uploadPerformanceForBooking(String url, Booking booking);

    List<PerformanceResponse> getAllPerformanceBelongToCurrentUser();

    List<PerformanceResponse> getAllPerformanceBelongToBooking(Integer bookingId);
}
