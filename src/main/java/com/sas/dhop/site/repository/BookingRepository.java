package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.Dancer;

import java.io.Serializable;
import java.util.List;

import com.sas.dhop.site.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByDancer(Dancer dancer);

    List<Booking> findAllByChoreography(Choreography choreography);

    List<Booking> findAllByCustomer(User customer);

}
