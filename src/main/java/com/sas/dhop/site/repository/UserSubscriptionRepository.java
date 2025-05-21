package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, Integer>, JpaSpecificationExecutor<UserSubscription> {}
