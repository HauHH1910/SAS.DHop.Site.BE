package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PerformanceRepository extends JpaRepository<Performance, Integer>, JpaSpecificationExecutor<Performance> {
}