package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.DanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DanceTypeRepository extends JpaRepository<DanceType, Integer>, JpaSpecificationExecutor<DanceType> {
}