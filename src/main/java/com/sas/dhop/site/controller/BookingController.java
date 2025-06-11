package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Booking")
@Tag(name = "[Booking Controller]")
@Slf4j(topic = "[Booking Controller]")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/get-all-booking")
    public ResponseData<List<BookingResponse>> getAllBooking() {
        return ResponseData.<List<BookingResponse>>builder()
                .message(ResponseMessage.GET_ALL_BOOKING)
                .data(bookingService.getAllBooking())
                .build();
    }

    @DeleteMapping("/cancel-booking/{bookingId}")
    public ResponseData<BookingResponse> cancelBooking(@PathVariable Integer bookingId){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.CANCEL_BOOKING)
                .data(bookingService.cancelBooking(bookingId))
                .build();
    }

    @PostMapping("/create-booking-for-choreographer")
    public ResponseData<BookingResponse> createBookingForChoreographer(@RequestBody BookingRequest bookingRequest) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.CREATE_BOOKING)
                .data(bookingService.createBookingRequestForChoreography(bookingRequest))
                .build();

    }

    @PostMapping("/create-booking-for-dancers")
    public ResponseData<BookingResponse> createBookingForDancers(@RequestBody BookingRequest bookingRequest){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.CREATE_BOOKING)
                .data(bookingService.createBookingRequestForDancer(bookingRequest))
                .build();
    }

}
