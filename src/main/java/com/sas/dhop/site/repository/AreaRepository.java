package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Area;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AreaRepository extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {
    Optional<Area> findByDistrict(String district);
}
