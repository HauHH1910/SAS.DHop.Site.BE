package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.Complain;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, Integer> {

    Optional<Complain> findByBooking(Booking booking);
}
