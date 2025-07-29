package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.UserSubscription;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Builder;

@Builder
public record UserSubscriptionResponse(
        Integer id,
        String subscriptionName,
        String status,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        Long remainingDays,
        String paymentLink) {

    public static UserSubscriptionResponse mapToResponse(UserSubscription subscription) {
        return UserSubscriptionResponse.builder()
                .id(subscription.getId())
                .subscriptionName(subscription.getSubscription().getName())
                .status(subscription.getStatus().getStatusName())
                .fromDate(subscription.getFromDate())
                .toDate(subscription.getToDate())
                .remainingDays(ChronoUnit.DAYS.between(LocalDate.now(), subscription.getToDate()))
                .build();
    }

    public static UserSubscriptionResponse mapToResponseWithUrl(UserSubscription subscription, String url) {
        return UserSubscriptionResponse.builder()
                .id(subscription.getId())
                .subscriptionName(subscription.getSubscription().getName())
                .status(subscription.getStatus().getStatusName())
                .fromDate(subscription.getFromDate())
                .toDate(subscription.getToDate())
                .remainingDays(ChronoUnit.DAYS.between(LocalDate.now(), subscription.getToDate()))
                .paymentLink(url)
                .build();
    }
}
