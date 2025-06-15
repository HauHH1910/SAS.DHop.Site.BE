package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatusRepository extends JpaRepository<Status, Integer>, JpaSpecificationExecutor<Status> {
    Optional<Status> findByStatusName(String statusName);
}
