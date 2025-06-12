package com.sas.dhop.site.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import vn.payos.type.CheckoutResponseData;

import java.util.Map;

public interface PaymentService {

    CheckoutResponseData createPaymentLink(CreatePaymentRequest request);

    ObjectNode getOrderByID(Long orderId);

    ObjectNode cancelOrder(Integer orderID);

    ObjectNode confirmWebHook(Map<String, String> request);
}
