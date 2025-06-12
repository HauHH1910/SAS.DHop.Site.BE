package com.sas.dhop.site.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.service.PaymentService;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Payment Service]")
public class PaymentServiceImpl implements PaymentService {

    private final PayOS payOS;

    @Override
    public CheckoutResponseData createPaymentLink(CreatePaymentRequest request) {
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
                    .returnUrl(request.returnUrl())
                    .cancelUrl(request.cancelUrl())
                    .build();

            return payOS.createPaymentLink(paymentData);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ObjectNode getOrderByID(Long orderId) {
        return null;
    }

    @Override
    public ObjectNode cancelOrder(Integer orderID) {
        return null;
    }

    @Override
    public ObjectNode confirmWebHook(Map<String, String> request) {
        return null;
    }
}
