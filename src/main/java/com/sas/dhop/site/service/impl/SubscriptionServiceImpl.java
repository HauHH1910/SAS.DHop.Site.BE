package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.repository.SubscriptionRepository;
import com.sas.dhop.site.service.SubscriptionService;
import com.sas.dhop.site.util.mapper.SubscriptionMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Subscription Service]")
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public List<SubscriptionResponse> getAllSubscription() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::mapToResponse)
                .toList();
    }
}
