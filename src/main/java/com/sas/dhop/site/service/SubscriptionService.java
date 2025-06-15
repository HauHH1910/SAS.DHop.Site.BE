package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.SubscriptionRequest;
import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.model.Subscription;
import java.util.List;

public interface SubscriptionService {

    List<SubscriptionResponse> getAllSubscription();

    SubscriptionResponse createSubscription(SubscriptionRequest request);

    SubscriptionResponse updateSubscription(Integer id, SubscriptionRequest request);

    SubscriptionResponse findSubscription(Integer id);

    Subscription findOrCreateSubscription(String status);

    void deleteSubscription(Integer id);

    SubscriptionResponse updateStatusSubscription(Integer id, boolean isUpdate);
}
