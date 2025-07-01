package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.SubscriptionPlan.*;

import com.sas.dhop.site.constant.SubscriptionStatus;
import com.sas.dhop.site.dto.request.SubscriptionRequest;
import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.Subscription;
import com.sas.dhop.site.repository.SubscriptionRepository;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.SubscriptionService;
import com.sas.dhop.site.util.mapper.SubscriptionMapper;

import java.math.BigDecimal;
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
    private final StatusService statusService;

    @Override
    public List<SubscriptionResponse> getAllSubscription() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::mapToResponse)
                .toList();
    }

    @Override
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        if (subscriptionRepository.findByName(request.name()).isPresent()) {
            throw new BusinessException(ErrorConstant.SUBSCRIPTION_EXIST);
        }
        log.info("[{}] is being created", request.name());
        Subscription subscription = subscriptionMapper.mapToModel(request);

        subscription.setStatus(statusService.findStatusOrCreated(SubscriptionStatus.ACTIVE));

        return subscriptionMapper.mapToResponse(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionResponse updateSubscription(Integer id, SubscriptionRequest request) {
        var subscription = subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND));
        log.info("[{}] is being updated", subscription.getName());

        subscriptionMapper.mapToUpdateUser(subscription, request);

        return subscriptionMapper.mapToResponse(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionResponse findSubscription(Integer id) {
        return subscriptionMapper.mapToResponse(subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND)));
    }

    @Override
    public void deleteSubscription(Integer id) {
        log.info("[{}] is being delete", id);
        subscriptionRepository.delete(subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND)));
    }

    @Override
    public Subscription findOrCreateSubscription(String status) {
        Status serviceStatusOrCreated = statusService.findStatusOrCreated(status);

        Subscription subscription = getSubscription(status, serviceStatusOrCreated);
        return subscriptionRepository
                .findByStatus(serviceStatusOrCreated)
                .orElseGet(() -> subscriptionRepository.save(subscription));
    }

    private Subscription getSubscription(String status, Status serviceStatusOrCreated) {
        Subscription subscription = new Subscription();

        switch (status) {
            case FREE_TRIAL -> {
                subscription.setPrice(BigDecimal.ZERO);
                subscription.setContent(FREE_TRIAL);
                subscription.setName(FREE_TRIAL);
                subscription.setStatus(serviceStatusOrCreated);
                subscription.setDuration(10);
            }

            case STANDARD_MONTHLY -> {
                subscription.setPrice(BigDecimal.valueOf(250000));
                subscription.setContent(STANDARD_MONTHLY);
                subscription.setName(STANDARD_MONTHLY);
                subscription.setStatus(serviceStatusOrCreated);
                subscription.setDuration(30);
            }
            case STANDARD_3MONTHS -> {
                subscription.setPrice(BigDecimal.valueOf(550000));
                subscription.setContent(STANDARD_3MONTHS);
                subscription.setName(STANDARD_3MONTHS);
                subscription.setStatus(serviceStatusOrCreated);
                subscription.setDuration(90);
            }
            case UNLIMITED_YEARLY -> {
                subscription.setPrice(BigDecimal.valueOf(1750000));
                subscription.setContent(UNLIMITED_YEARLY);
                subscription.setName(UNLIMITED_YEARLY);
                subscription.setStatus(serviceStatusOrCreated);
                subscription.setDuration(365);
            }
            default -> {
                throw new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND);
            }
        }
        return subscription;
    }

    @Override
    public SubscriptionResponse updateStatusSubscription(Integer id, boolean isUpdate) {
        final Subscription subscription = subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND));
        if (isUpdate) {
            if (SubscriptionStatus.ACTIVE.equals(subscription.getStatus().getStatusName())) {
                subscription.setStatus(statusService.findStatusOrCreated(SubscriptionStatus.INACTIVE));
            } else {
                subscription.setStatus(statusService.findStatusOrCreated(SubscriptionStatus.ACTIVE));
            }
            subscriptionRepository.save(subscription);
            log.info(
                    "[{}] is being updated to [{}]",
                    subscription.getName(),
                    subscription.getStatus().getStatusName());
        }
        return subscriptionMapper.mapToResponse(subscription);
    }
}
