package com.sas.dhop.site.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import vn.payos.type.PaymentLinkData;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Payment Service]")
public class PaymentServiceImpl implements PaymentService {

  private final PayOS payOS;
  private final ObjectMapper objectMapper;

  @Override
  public ObjectNode createPaymentLink(CreatePaymentRequest request) {
    ObjectNode response = objectMapper.createObjectNode();
    try {
      String currentTimeString = String.valueOf(new Date().getTime());

      long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

      ItemData item =
          ItemData.builder().name(request.name()).price(request.price()).quantity(1).build();

      PaymentData paymentData =
          PaymentData.builder()
              .orderCode(orderCode)
              .description(request.description())
              .amount(request.price())
              .item(item)
              .returnUrl(request.returnUrl())
              .cancelUrl(request.cancelUrl())
              .build();

      CheckoutResponseData data = payOS.createPaymentLink(paymentData);

      response.put("error", 0);
      response.put("message", "success");
      response.set("data", objectMapper.valueToTree(data));
      return response;

    } catch (Exception e) {
      e.printStackTrace();
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
      e.printStackTrace();
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
      e.printStackTrace();
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
      e.printStackTrace();
      response.put("error", -1);
      response.put("message", e.getMessage());
      response.set("data", null);
      return response;
    }
  }
}
