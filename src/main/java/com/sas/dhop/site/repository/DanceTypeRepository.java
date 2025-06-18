package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.DanceType;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DanceTypeRepository extends JpaRepository<DanceType, Integer>, JpaSpecificationExecutor<DanceType> {
    @Query("select d from DanceType d where d.type = :type")
    Optional<DanceType> findByType(@Param("type") String type);

}
