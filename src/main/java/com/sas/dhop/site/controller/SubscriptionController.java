package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.SubscriptionRequest;
import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.service.SubscriptionService;
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

    private final SubscriptionService service;

    @GetMapping
    public ResponseData<List<SubscriptionResponse>> getSubscription() {
        return ResponseData.<List<SubscriptionResponse>>builder()
                .data(service.getAllSubscription())
                .message(ResponseMessage.GET_SUBSCRIPTION)
                .build();
    }

    @PostMapping
    public ResponseData<SubscriptionResponse> createNewSubscription(@RequestBody SubscriptionRequest request) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(service.createSubscription(request))
                .message(ResponseMessage.CREATE_SUBSCRIPTION)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseData<SubscriptionResponse> updateSubscriptionById(
            @PathVariable("id") Integer id, @RequestBody SubscriptionRequest request) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(service.updateSubscription(id, request))
                .message(ResponseMessage.UPDATE_SUBSCRIPTION)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<SubscriptionResponse> findSubscriptionById(@PathVariable("id") Integer id) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(service.findSubscription(id))
                .message(ResponseMessage.FIND_SUBSCRIPTION)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteSubscriptionById(@PathVariable("id") Integer id) {
        service.deleteSubscription(id);
        return ResponseData.<Void>builder()
                .message(ResponseMessage.DELETE_SUBSCRIPTION)
                .build();
    }

    @PatchMapping("/status/{id}")
    public ResponseData<SubscriptionResponse> statusSubscriptionUpdate(
            @PathVariable("id") Integer id, @RequestBody boolean isUpdate) {
        return ResponseData.<SubscriptionResponse>builder()
                .data(service.updateStatusSubscription(id, isUpdate))
                .message(ResponseMessage.UPDATE_SUBSCRIPTION)
                .build();
    }
}
