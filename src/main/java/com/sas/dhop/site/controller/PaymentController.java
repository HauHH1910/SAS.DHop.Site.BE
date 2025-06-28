package com.sas.dhop.site.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.dto.request.CommissionPaymentRequest;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.model.Payment;
import com.sas.dhop.site.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "[Payment Controller]")
@Slf4j(topic = "[Payment Controller]")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentLink(request));
    }

    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        return paymentService.getOrderByID(orderId);
    }

    @PutMapping("/{order-id}")
    public ObjectNode cancelOrder(@PathVariable("order-id") int orderId) {
        return paymentService.cancelOrder(orderId);
    }

    @PostMapping("/confirm-webhook")
    public ObjectNode confirmWebHook(@RequestBody Map<String, String> request) {
        return paymentService.confirmWebHook(request);
    }

    @PostMapping("/commission")
    public ObjectNode paymentCommission(@RequestBody CommissionPaymentRequest request) {
        return paymentService.commissionPayment(request);
    }

    @PostMapping("/callback")
    public ResponseEntity<Payment> callBack(
            @RequestParam("status") String status, @RequestParam("orderCode") Long orderCode) throws Exception {
        return ResponseEntity.ok(paymentService.saveCommissionPayment(status, orderCode));
    }
}
