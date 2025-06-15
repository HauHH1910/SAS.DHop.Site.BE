package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Choreographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoreographerRepository extends JpaRepository<Choreographer, Integer> {
    List<Choreographer> findTop5ByOrderByPriceDesc();
} 