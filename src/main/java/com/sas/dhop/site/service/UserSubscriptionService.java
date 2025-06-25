package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.UserSubscription;

import java.util.List;

public interface UserSubscriptionService {

    List<UserSubscriptionResponse> getSubscriptionStatus();

    void addOrForceToBuySubscription(Integer userId);

    void updateSubscriptionStatus();

    UserSubscriptionResponse buySubscription(Integer id);

    UserSubscription findUserSubscriptionByUser(User user);

    Integer countBookingFromUserSubscription(UserSubscription userSubscription);
}
