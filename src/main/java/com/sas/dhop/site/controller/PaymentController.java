package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.type.CheckoutResponseData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "[Payment Controller]")
@Slf4j(topic = "[Payment Controller]")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseData<CheckoutResponseData> createPayment(@RequestBody CreatePaymentRequest request) {
        return ResponseData.<CheckoutResponseData>builder()
                .message(ResponseMessage.CREATE_PAYMENT)
                .data(paymentService.createPaymentLink(request))
                .build();
    }
}
