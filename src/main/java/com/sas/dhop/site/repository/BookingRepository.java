package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByDancer(Dancer dancer);

    List<Booking> findAllByChoreography(Choreography choreography);

    List<Booking> findAllByCustomer(User customer);

    Optional<Booking> findByBookingDate(LocalDateTime bookingDate);

    //    List<Booking> findAllByBookingIdAndStatus(Integer bookingId, Status status);

    long countBookingByDancer(Dancer dancer);

    @Query(
            "select count(b) from Booking b where b.dancer = ?1 and b.bookingDate between ?2 and ?3 and b.status.statusName = 'Đã hoàn tất'")
    long countBookingByDancerAndBookingDateBetween(
            Dancer dancer, LocalDateTime bookingDateAfter, LocalDateTime bookingDateBefore);

    List<Booking> findAllByStatus(Status status);
}
