package com.sas.dhop.site.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.constant.PaymentStatus;
import com.sas.dhop.site.dto.request.CommissionPaymentRequest;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Payment;
import com.sas.dhop.site.repository.PaymentRepository;
import com.sas.dhop.site.service.PaymentService;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Payment Service]")
public class PaymentServiceImpl implements PaymentService {

    private final PayOS payOS;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Value("${sas.payos.url}")
    private String url;

    @Override
    public ObjectNode commissionPayment(CommissionPaymentRequest request) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String currentTimeString = String.valueOf(new Date().getTime());

            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder()
                    .name(request.name())
                    .price(request.price())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(request.description())
                    .amount(request.price())
                    .item(item)
                    .returnUrl(url + "/payment-success")
                    .cancelUrl(url + "/payment-error")
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            paymentRepository.save(new Payment(orderCode, PaymentStatus.PAID, paymentData.getAmount()));
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;
        } catch (Exception e) {
            log.info("[commission payment] - [{}]", e.getMessage());
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    @Override
    public String createPaymentLinkForBuyingSubscription(CreatePaymentRequest request) {
        try {
            String currentTimeString = String.valueOf(new Date().getTime());

            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder()
                    .name(request.name())
                    .price(request.price())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(request.description())
                    .amount(request.price())
                    .item(item)
                    .returnUrl(url + "/subscription-status")
                    .cancelUrl(url + "/subscription-status")
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            paymentRepository.save(new Payment(orderCode, PaymentStatus.PAID, paymentData.getAmount()));

            return data.getCheckoutUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectNode getOrderByID(Long orderId) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.info("[get order by id] - [{}]", e.getMessage());
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode cancelOrder(Integer orderID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderID, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.info("[cancel order] - [{}]", e.getMessage());
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode confirmWebHook(Map<String, String> request) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(request.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.info("[confirm web hook] - [{}]", e.getMessage());
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public Payment findPayment(Long orderCode) {
        return paymentRepository
                .findByOrderCode(orderCode)
                .orElseThrow(() -> new BusinessException(ErrorConstant.PAYMENT_NOT_FOUND));
    }

    @Override
    public Payment savePayment(String status, Long orderCode) throws Exception {
        PaymentLinkData order = payOS.getPaymentLinkInformation(orderCode);
        log.info("[Save Payment] - [Status: {} - Order Code: {}]", order.getStatus(), order.getOrderCode());

        Payment existingPayment = paymentRepository.findByOrderCode(orderCode).orElse(null);

        if (existingPayment != null) {
            existingPayment.setStatus(PaymentStatus.PAID);
            return paymentRepository.save(existingPayment);
        } else {
            Payment newPayment = new Payment(order.getOrderCode(), PaymentStatus.PAID, order.getAmount());
            return paymentRepository.save(newPayment);
        }
    }
}
