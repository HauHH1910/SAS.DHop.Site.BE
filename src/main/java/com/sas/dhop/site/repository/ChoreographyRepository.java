package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChoreographyRepository
        extends JpaRepository<Choreography, Integer>, JpaSpecificationExecutor<Choreography> {
    List<Choreography> findByStatus(Status status);
}
