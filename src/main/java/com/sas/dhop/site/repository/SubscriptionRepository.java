package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Subscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionRepository
    extends JpaRepository<Subscription, Integer>, JpaSpecificationExecutor<Subscription> {
  Optional<Subscription> findByName(String name);
}
