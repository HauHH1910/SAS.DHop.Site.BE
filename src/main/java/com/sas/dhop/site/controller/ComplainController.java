package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.ComplainRequest;
import com.sas.dhop.site.dto.response.ComplainResponse;
import com.sas.dhop.site.service.ComplainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Complains")
@Tag(name = "[Complains Controller]")
@Slf4j(topic = "[Complains Controller]")
public class ComplainController {

    private final ComplainService complainService;

    @PostMapping("/complains-request")
    public ResponseData<ComplainResponse> bookingComplains(@RequestBody ComplainRequest complainRequest) {
        return ResponseData.<ComplainResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_REQUEST)
                .data(complainService.bookingComplains(complainRequest))
                .build();
    }

    @PutMapping("/booking-complains-accept/{bookingId}")
    public ResponseData<ComplainResponse> acceptBookingComplainsProgress(@PathVariable Integer bookingId) {
        return ResponseData.<ComplainResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_APPLY)
                .data(complainService.approveBookingComplains(bookingId))
                .build();
    }

    @PutMapping("/booking-complains-deny/{bookingId}")
    public ResponseData<ComplainResponse> denyBookingComplainsProgress(@PathVariable Integer bookingId) {
        return ResponseData.<ComplainResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_DENY)
                .data(complainService.cancelBookingComplains(bookingId))
                .build();
    }

    @PutMapping("/booking-complains-complete/{bookingId}")
    public ResponseData<ComplainResponse> completeBookingComplainsProgress(@PathVariable Integer bookingId) {
        return ResponseData.<ComplainResponse>builder()
                .message(ResponseMessage.BOOKING_COMPLAINS_COMPLETE)
                .data(complainService.completeBookingComplains(bookingId))
                .build();
    }
}
