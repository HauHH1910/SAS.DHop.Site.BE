package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChoreographyRepository
    extends JpaRepository<Choreography, Integer>, JpaSpecificationExecutor<Choreography> {
  List<Choreography> findByStatus(Status status);
}
