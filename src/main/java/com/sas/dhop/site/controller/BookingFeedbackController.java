package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.BookingFeedbackRequest;
import com.sas.dhop.site.dto.response.BookingFeedbackResponse;
import com.sas.dhop.site.service.BookingFeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Feedback")
@Tag(name = "[Booking Feedback Controller]")
@Slf4j(topic = "[Booking Feedback Controller]")
public class BookingFeedbackController {
    private final BookingFeedbackService bookingFeedbackService;

    @PostMapping("/create-booking-feedback")
    public ResponseData<BookingFeedbackResponse> createBookingFeedback(@RequestBody BookingFeedbackRequest bookingFeedbackRequest){
        return ResponseData.<BookingFeedbackResponse>builder()
                .message(ResponseMessage.CREATE_BOOKING_FEEDBACK)
                .data(bookingFeedbackService.createBookingFeedback(bookingFeedbackRequest))
                .build();
    }

    @GetMapping("/get-feedback-of-dancer/{dancerId}")
    public ResponseData<List<BookingFeedbackResponse>> getBookingFeedbackOfDancer (@PathVariable Integer dancerId){
        return ResponseData.<List<BookingFeedbackResponse>>builder()
                .message(ResponseMessage.GET_FEEBACK_FOR_DANCER)
                .data(bookingFeedbackService.getFeedbackByDancerId(dancerId))
                .build();
    }

    @GetMapping("/get-feedback-of-choreographer/{choreographerId}")
    public ResponseData<List<BookingFeedbackResponse>> getBookingFeedbackOfChoreographer (@PathVariable Integer choreographerId){
        return ResponseData.<List<BookingFeedbackResponse>>builder()
                .message(ResponseMessage.GET_FEEBACK_FOR_CHOREOGRAPHER)
                .data(bookingFeedbackService.getFeedbackByChoreographer(choreographerId))
                .build();
    }

    @GetMapping("/get-feedback-of-booking/{bookingId}")
    public ResponseData<BookingFeedbackResponse> getBookingFeedbackByBookingID (@PathVariable Integer bookingId){
        return ResponseData.<BookingFeedbackResponse>builder()
                .message(ResponseMessage.GET_BOOKING_FEEDBACK)
                .data(bookingFeedbackService.getBookingFeedbackByBookingId(bookingId))
                .build();
    }

}
