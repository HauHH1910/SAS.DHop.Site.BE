package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    List<UserSubscription> findByUser_Id(Integer userId);
    Optional<UserSubscription> findByUserId(Integer userId);
}
