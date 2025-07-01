package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByDancer(Dancer dancer);

    List<Booking> findAllByChoreography(Choreography choreography);

    List<Booking> findAllByCustomer(User customer);

    Optional<Booking> findByBookingDate(LocalDateTime bookingDate);

    //    List<Booking> findAllByBookingIdAndStatus(Integer bookingId, Status status);

    List<Booking> findAllByStatus(Status status);
}
