package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Dancer;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.Subscription;
import com.sas.dhop.site.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DancerRepository extends JpaRepository<Dancer, Integer>, JpaSpecificationExecutor<Dancer> {
    Optional<Dancer> findByUser(User user);

    List<Dancer> findByStatus(Status status);

    List<Dancer> findTop5ByOrderByPriceDesc();

    @Query("select d from Dancer d where d.subscription = ?1")
    List<Dancer> findDancerBySubscription(Subscription subscription);
}
