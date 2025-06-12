package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ResponseData<BookingCancelResponse> cancelBooking(@PathVariable Integer bookingId) {
        return ResponseData.<BookingCancelResponse>builder()
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
    public ResponseData<BookingResponse> createBookingForDancers(@RequestBody BookingRequest bookingRequest) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.CREATE_BOOKING)
                .data(bookingService.createBookingRequestForDancer(bookingRequest))
                .build();
    }

    @PutMapping("/accept/{bookingId}")
    public ResponseData<BookingResponse> acceptBookingRequest(@PathVariable Integer bookingId) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.ACCEPT_BOOKING_SUCESSFULLY)
                .data(bookingService.acceptBookingRequest(bookingId))
                .build();
    }

    @PutMapping("/start-work-request/{bookingId}")
    public ResponseData<BookingResponse> startWorkRequest(@PathVariable Integer bookingId){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.START_WORK_SUCCESSFULLY)
                .data(bookingService.startWork(bookingId))
                .build();
    }

    @PutMapping("/end-work-request/{bookingId}")
    public ResponseData<BookingResponse> endWorkRequest(@PathVariable Integer bookingId){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.END_WORK_SUCCESSFULLY)
                .data(bookingService.startWork(bookingId))
                .build();
    }




//    public ResponseData<BookingResponse>

}
