package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Performance;
import com.sas.dhop.site.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PerformanceRepository
        extends JpaRepository<Performance, Integer>, JpaSpecificationExecutor<Performance> {
    List<Performance> findByUser(User user);

    @Query("SELECT p FROM Performance p WHERE p.booking.id = :id")
    List<Performance> findByBooking(Integer id);


}
