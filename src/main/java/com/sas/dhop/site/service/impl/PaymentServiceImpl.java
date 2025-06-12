package com.sas.dhop.site.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final PayOS payOS;

    public CheckoutResponseData createPaymentUrl() throws Exception {
        //        ItemData itemData = ItemData.builder().name("Mỳ tôm Hảo Hảo ly").quantity(1).price(2000).build();
        //        PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(2000)
        //                .description("Thanh toán đơn hàng").returnUrl(webhookUrl + "/success").cancelUrl(webhookUrl +
        // "/cancel")
        //                .item(itemData).build();
        //        return payOS.createPaymentLink(paymentData);
        return null;
    }
}
