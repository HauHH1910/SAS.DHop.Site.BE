package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.UserSubscriptionStatus.*;
import static com.sas.dhop.site.dto.response.UserSubscriptionResponse.mapToResponse;

import com.sas.dhop.site.dto.request.UserSubscriptionRequest;
import com.sas.dhop.site.dto.response.UserSubscriptionResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.Subscription;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.UserSubscription;
import com.sas.dhop.site.repository.SubscriptionRepository;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.repository.UserSubscriptionRepository;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserSubscriptionService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[User Subscription Service]")
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
	private final UserRepository userRepository;
	private final UserSubscriptionRepository userSubscriptionRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final StatusService statusService;

	@Override
	public List<UserSubscriptionResponse> getSubscriptionStatus() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("[get subscription status] - [{}]", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

		List<UserSubscription> list = userSubscriptionRepository.findByUser_Id(user.getId());

		List<UserSubscriptionResponse> responsesList = new ArrayList<>();

		list.forEach(subscription -> {
			log.info("[get subscription status] - [{}]", subscription.getSubscription().getName());
			responsesList.add(mapToResponse(subscription));
		});
		return responsesList;
	}

	@Override
	public UserSubscriptionResponse addSubscriptionToUser(UserSubscriptionRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("[add subscription to user] - [{}]", email);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

		Subscription subscription = subscriptionRepository.findById(request.subscriptionId())
				.orElseThrow(() -> new BusinessException(ErrorConstant.SUBSCRIPTION_NOT_FOUND));

		Status status = statusService.findStatusOrCreated(ACTIVE_USER_SUBSCRIPTION);

		return mapToResponse(userSubscriptionRepository.save(UserSubscription.builder().user(user)
				.subscription(subscription).fromDate(Instant.now())
				.toDate(Instant.now().plus(subscription.getDuration(), ChronoUnit.DAYS)).status(status).build()));
	}

	@Override
	public void updateSubscriptionStatus() {
	}
}
