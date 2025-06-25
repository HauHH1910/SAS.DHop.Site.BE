package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.SubscriptionRequest;
import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import com.sas.dhop.site.service.SubscriptionService;
import com.sas.dhop.site.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@Tag(name = "Subscription Controller")
@Slf4j(topic = "[Subscription Controller]")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserSubscriptionService userSubscriptionService;

    @GetMapping
    public ResponseData<List<SubscriptionResponse>> getSubscription() {
        return ResponseData.<List<SubscriptionResponse>>builder()
                .data(subscriptionService.getAllSubscription())
                .message(ResponseMessage.GET_SUBSCRIPTION)
                .build();
    }

    @PostMapping
    public ResponseData<SubscriptionResponse> createNewSubscription(@RequestBody SubscriptionRequest request) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(subscriptionService.createSubscription(request))
                .message(ResponseMessage.CREATE_SUBSCRIPTION)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseData<SubscriptionResponse> updateSubscriptionById(
            @PathVariable("id") Integer id, @RequestBody SubscriptionRequest request) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(subscriptionService.updateSubscription(id, request))
                .message(ResponseMessage.UPDATE_SUBSCRIPTION)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<SubscriptionResponse> findSubscriptionById(@PathVariable("id") Integer id) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(subscriptionService.findSubscription(id))
                .message(ResponseMessage.FIND_SUBSCRIPTION)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteSubscriptionById(@PathVariable("id") Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseData.<Void>builder()
                .message(ResponseMessage.DELETE_SUBSCRIPTION)
                .build();
    }

    @PatchMapping("/status/{id}")
    public ResponseData<SubscriptionResponse> statusSubscriptionUpdate(
            @PathVariable("id") Integer id, @RequestBody boolean isUpdate) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(subscriptionService.updateStatusSubscription(id, isUpdate))
                .message(ResponseMessage.UPDATE_SUBSCRIPTION)
                .build();
    }

    @PostMapping("/pay/{id}")
    public ResponseData<UserSubscriptionResponse> buySubscription(@PathVariable Integer id) {
        return ResponseData.<UserSubscriptionResponse>builder()
                .data(userSubscriptionService.buySubscription(id))
                .build();
    }
}
