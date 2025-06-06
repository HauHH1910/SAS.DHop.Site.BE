package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Dancer;
import com.sas.dhop.site.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DancerRepository extends JpaRepository<Dancer, Integer>, JpaSpecificationExecutor<Dancer> {
    Optional<Dancer> findByUser(User user);
}
