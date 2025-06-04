package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingResponse;
import java.util.List;

public interface BookingService {

    // create booking contract (add field to insert information in contract), set booking status is Pending
    BookingResponse createBookingRequest(BookingRequest bookingRequest);

    // Set booking status is Booking_Activate
    BookingResponse acceptBookingRequest(int bookingId);

    // Set booking status is Booking_InProgress if system have no problem
    BookingResponse startWork(int bookingId);

    BookingResponse getBookingDetail(int bookingId);

    List<BookingResponse> getAllBooking();

    // Set booking status is Booking_WorkDone if dancer push the end work button
    // EndBookingResponse endWork(int bookingId);

}
