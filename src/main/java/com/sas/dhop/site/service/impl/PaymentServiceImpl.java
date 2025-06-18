package com.sas.dhop.site.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.constant.PaymentStatus;
import com.sas.dhop.site.dto.request.CommissionPaymentRequest;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.Payment;
import com.sas.dhop.site.repository.BookingRepository;
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
    private final BookingRepository bookingRepository;

    @Value("${sas.payos.return-url}")
    private String returnUrl;

    @Value("${sas.payos.cancel-url}")
    private String cancelUrl;

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
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
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
    public ObjectNode createPaymentLink(CreatePaymentRequest request) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            Booking booking = bookingRepository.findById(request.bookingId())
                    .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
            String currentTimeString = String.valueOf(booking.getBookingDate());

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
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            paymentRepository.save(new Payment(orderCode, "", paymentData.getAmount()));
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            log.info("[create payment link] - [{}]", e.getMessage());
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
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
    public Payment saveCommissionPayment(String status, Long orderCode) throws Exception {
        PaymentLinkData order = payOS.getPaymentLinkInformation(orderCode);
        if (order == null) {
            throw new BusinessException(ErrorConstant.PAYMENT_NOT_FOUND);
        }
        return paymentRepository.findByOrderCode(orderCode)
                .orElseGet(() -> paymentRepository.save(new Payment(orderCode, status, order.getAmount())));
    }
}
