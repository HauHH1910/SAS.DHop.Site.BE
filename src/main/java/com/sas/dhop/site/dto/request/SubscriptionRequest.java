package com.sas.dhop.site.dto.request;

import java.math.BigDecimal;

public record SubscriptionRequest(String name, Integer duration, String content, BigDecimal price) {
}
