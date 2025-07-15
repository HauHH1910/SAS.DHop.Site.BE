package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.request.DancerAcceptRequest;
import com.sas.dhop.site.dto.request.DancerBookingRequest;
import com.sas.dhop.site.dto.request.EndWorkRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBookingRequestForDancer(DancerBookingRequest request);

    BookingResponse createBookingRequestForChoreography(BookingRequest bookingRequest);

    BookingResponse acceptBookingRequest(int bookingId, DancerAcceptRequest request);

    BookingResponse startWork(int bookingId);

    BookingResponse getBookingDetail(int bookingId);

    List<BookingResponse> getAllBooking();

    BookingCancelResponse cancelBooking(int bookingId);

    BookingResponse completeWork(int bookingId);

    BookingResponse dancerAcceptBooking(Integer bookingId);

    BookingResponse userSentPayment(EndWorkRequest request);

    BookingResponse endBooking(int bookingId);

    BookingResponse updateBookingInformation(Integer bookingId, BookingRequest bookingRequest);

    BookingResponse endWorking(EndWorkRequest request);

    BookingCancelResponse bookingComplains(Integer bookingId);

    BookingResponse acceptBookingComplainsProgress(Integer bookingId);

    BookingResponse denyBookingComplainsProgress(Integer bookingId);

    List<BookingResponse> findBookingByAuthenticatedUser();
}
