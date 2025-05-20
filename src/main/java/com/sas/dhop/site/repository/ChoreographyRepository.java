package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Choreography;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChoreographyRepository extends JpaRepository<Choreography, Integer>, JpaSpecificationExecutor<Choreography> {
}