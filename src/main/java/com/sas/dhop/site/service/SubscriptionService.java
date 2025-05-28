package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.SubscriptionResponse;
import java.util.List;

public interface SubscriptionService {

    List<SubscriptionResponse> getAllSubscription();
}
