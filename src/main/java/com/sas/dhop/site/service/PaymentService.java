package com.sas.dhop.site.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sas.dhop.site.dto.request.CommissionPaymentRequest;
import com.sas.dhop.site.dto.request.CreatePaymentRequest;
import com.sas.dhop.site.model.Payment;
import java.util.Map;

public interface PaymentService {

    ObjectNode commissionPayment(CommissionPaymentRequest request);

    ObjectNode createPaymentLink(CreatePaymentRequest request);

    ObjectNode getOrderByID(Long orderId);

    ObjectNode cancelOrder(Integer orderID);

    ObjectNode confirmWebHook(Map<String, String> request);

    Payment findPayment(Long orderCode);
}
