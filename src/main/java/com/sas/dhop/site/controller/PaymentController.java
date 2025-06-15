package com.sas.dhop.site.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "[Payment Controller]")
@Slf4j(topic = "[Payment Controller]")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseData<ObjectNode> createPayment(@RequestBody CreatePaymentRequest request) {
        return ResponseData.<ObjectNode>builder()
                .message(ResponseMessage.CREATE_PAYMENT)
                .data(paymentService.createPaymentLink(request))
                .build();
    }

    @GetMapping(path = "/{orderId}")
    public ResponseData<ObjectNode> getOrderById(@PathVariable("orderId") long orderId) {
        return ResponseData.<ObjectNode>builder()
                .data(paymentService.getOrderByID(orderId))
                .build();
    }


    @PutMapping("/{order-id}")
    public ResponseData<ObjectNode> cancelOrder(@PathVariable("order-id") int orderId) {
        return ResponseData.<ObjectNode>builder()
                .data(paymentService.cancelOrder(orderId))
                .build();
    }

    @PostMapping("/confirm-webhook")
    public ResponseData<ObjectNode> confirmWebHook(@RequestBody Map<String, String> request) {
        return ResponseData.<ObjectNode>builder()
                .data(paymentService.confirmWebHook(request))
                .build();
    }
}
