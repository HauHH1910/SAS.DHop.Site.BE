package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, Integer> {
}
