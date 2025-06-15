package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.request.DancerBookingRequest;
import com.sas.dhop.site.dto.request.EndWorkRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import java.util.List;

public interface BookingService {

    // create booking contract for dancer(add field to insert information in
    // contract), set booking
    // status is Pending
    BookingResponse createBookingRequestForDancer(DancerBookingRequest request);

    // create booking contract for choreography(add field to insert information in
    // contract), set
    // booking status is
    // Pending
    BookingResponse createBookingRequestForChoreography(BookingRequest bookingRequest);

    // Set booking status is Booking_Activate
    BookingResponse acceptBookingRequest(int bookingId);

    // Set booking status is Booking_InProgress if system have no problem
    BookingResponse startWork(int bookingId);

    BookingResponse getBookingDetail(int bookingId);

    List<BookingResponse> getAllBooking();

    BookingCancelResponse cancelBooking(int bookingId);

    // BookingResponse confirmWork(int bookingId);

    BookingResponse endBooking(int bookingId);

    BookingResponse updateBookingInformation(Integer bookingId, BookingRequest bookingRequest);

    BookingResponse endWorking(EndWorkRequest request);

    // For user(booker, dancers, choreographer) use
    BookingCancelResponse bookingComplains(Integer bookingId);

    // For Staff process complains and change booking status
    BookingResponse acceptBookingComplainsProgress(Integer bookingId);

    // For Staff process complains and change booking status
    BookingResponse denyBookingComplainsProgress(Integer bookingId);
}
