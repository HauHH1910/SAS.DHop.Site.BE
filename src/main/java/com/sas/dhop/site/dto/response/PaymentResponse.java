package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Payment;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.sas.dhop.site.model.Payment}
 */
@Value
@Builder
public class PaymentResponse implements Serializable {
    Integer id;
    LocalDateTime createdAt;
    Long orderCode;
    String status;
    Integer amount;


    public static PaymentResponse mapToPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .createdAt(payment.getCreatedAt())
                .orderCode(payment.getOrderCode())
                .status(payment.getStatus())
                .build();
    }
}