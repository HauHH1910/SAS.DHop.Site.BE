package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.request.EndWorkRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Booking")
@Tag(name = "[Booking Controller]")
@Slf4j(topic = "[Booking Controller]")
public class BookingController {
    private final BookingService bookingService;
    private final BuildProperties buildProperties;

    @GetMapping("/get-all-booking")
    public ResponseData<List<BookingResponse>> getAllBooking() {
        return ResponseData.<List<BookingResponse>>builder()
                .message(ResponseMessage.GET_ALL_BOOKING)
                .data(bookingService.getAllBooking())
                .build();
    }

    @GetMapping("/get-booking-detail/{bookingId}")
    public ResponseData<BookingResponse> getBookingDetail(@PathVariable Integer bookingId) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.GET_BOOKING_DETAIL)
                .data(bookingService.getBookingDetail(bookingId))
                .build();
    }

    @DeleteMapping("/cancel-booking/{bookingId}")
    public ResponseData<BookingCancelResponse> cancelBooking(@PathVariable Integer bookingId) {
        return ResponseData.<BookingCancelResponse>builder()
                .message(ResponseMessage.CANCEL_BOOKING)
                .data(bookingService.cancelBooking(bookingId))
                .build();
    }

    @PutMapping("/update-booking/{bookingId}")
    public ResponseData<BookingResponse> updateBookingInformation(@PathVariable Integer bookingId, @RequestBody BookingRequest bookingRequest) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.UPDATE_BOOKING_SUCCESSFULLY)
                .data(bookingService.updateBookingInformation(bookingId, bookingRequest))
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
    public ResponseData<BookingResponse> startWorkRequest(@PathVariable Integer bookingId) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.START_WORK_SUCCESSFULLY)
                .data(bookingService.startWork(bookingId))
                .build();
    }

    @PutMapping("/end-work-request")
    public ResponseData<BookingResponse> endWorkRequest(@RequestBody EndWorkRequest endWorkRequest) {
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.END_WORK_SUCCESSFULLY)
                .data(bookingService.endWorking(endWorkRequest))
                .build();
    }

    @PutMapping("/booking-complains/{bookingId}")
    public ResponseData<BookingCancelResponse> bookingComplains(@PathVariable Integer bookingId){
        return ResponseData.<BookingCancelResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_REQUEST)
                .data(bookingService.bookingComplains(bookingId))
                .build();
    }

    @PutMapping("/booking-complains-accept/{bookingId}")
    public ResponseData<BookingResponse> acceptBookingComplainsProgress(@PathVariable Integer bookingId){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_APPLY)
                .data(bookingService.acceptBookingComplainsProgress(bookingId))
                .build();
    }

    @PutMapping("/booking-complains-deny/{bookingId}")
    public ResponseData<BookingResponse> denyBookingComplainsProgress(@PathVariable Integer bookingId){
        return ResponseData.<BookingResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_DENY)
                .data(bookingService.denyBookingComplainsProgress(bookingId))
                .build();
    }


    //    public ResponseData<BookingResponse>

}
