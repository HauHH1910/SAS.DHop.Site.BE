package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.UserSubscription;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Builder;

@Builder
public record UserSubscriptionResponse(
        String subscriptionName, String status, Instant fromDate, Instant toDate, Long remainingDays) {

    public static UserSubscriptionResponse mapToResponse(UserSubscription subscription) {
        return UserSubscriptionResponse.builder()
                .subscriptionName(subscription.getSubscription().getName())
                .status(subscription.getStatus().getStatusName())
                .fromDate(subscription.getFromDate())
                .toDate(subscription.getToDate())
                .remainingDays(ChronoUnit.DAYS.between(LocalDate.now(), subscription.getToDate()))
                .build();
    }
}
