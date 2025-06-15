package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.UserSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {

    Optional<UserSubscription> findByUser(User user);

    List<UserSubscription> findAllByUser(User user);
}
