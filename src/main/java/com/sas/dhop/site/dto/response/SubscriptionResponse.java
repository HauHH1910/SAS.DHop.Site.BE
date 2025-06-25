package com.sas.dhop.site.dto.response;

import java.math.BigDecimal;

public record SubscriptionResponse(Integer id, String name, String content, BigDecimal price) {
}
