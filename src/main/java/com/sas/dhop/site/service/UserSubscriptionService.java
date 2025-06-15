package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.UserSubscriptionRequest;
import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import java.util.List;

public interface UserSubscriptionService {

  List<UserSubscriptionResponse> getSubscriptionStatus();

  UserSubscriptionResponse addSubscriptionToUser(UserSubscriptionRequest request);

  void updateSubscriptionStatus();
}
