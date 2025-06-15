package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository
    extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

  //    // Get total bookings between start and end date
  //    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
  //    Long countBookingsBetweenDates(@Param("startDate") LocalDateTime startDate,
  // @Param("endDate") LocalDateTime
  // endDate);
  //
  //    // Get total bookings in current month
  //    @Query("SELECT COUNT(b) FROM Booking b WHERE YEAR(b.bookingDate) = YEAR(CURRENT_DATE) AND
  // MONTH(b.bookingDate)
  // = MONTH(CURRENT_DATE)")
  //    Long countBookingsInCurrentMonth();
  //
  //    // Get total bookings in current year
  //    @Query("SELECT COUNT(b) FROM Booking b WHERE YEAR(b.bookingDate) = YEAR(CURRENT_DATE)")
  //    Long countBookingsInCurrentYear();
  //
  //    // Get total bookings in current week
  //    @Query("SELECT COUNT(b) FROM Booking b WHERE YEAR(b.bookingDate) = YEAR(CURRENT_DATE) AND
  // WEEK(b.bookingDate)
  // = WEEK(CURRENT_DATE)")
  //    Long countBookingsInCurrentWeek();
  //
  //    // Get total bookings by status
  //    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status.statusName = :status")
  //    Long countBookingsByStatus(@Param("status") String status);
  //
  //    // Get total amount of activated bookings
  //    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE b.status.statusName =
  // 'BOOKING_ACTIVATE'")
  //    BigDecimal getTotalAmountOfActivatedBookings();
  //
  //    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE b.status.statusName =
  // 'BOOKING_ACTIVATE' AND
  // b.dancer.id = :dancerId")
  //    BigDecimal getTotalAmountOfActivatedBookingsByDancerId(@Param("dancerId") Integer dancerId);
  //
  //    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE b.status.statusName =
  // 'BOOKING_ACTIVATE' AND
  // b.choreography.id = :choreographyId")
  //    BigDecimal getTotalAmountOfActivatedBookingsByChoreographyId(@Param("choreographyId")
  // Integer choreographyId);
  //
  //    List<Booking> findTop5ByOrderByBookingDateDesc();
  //    List<Booking> findByDancerId(Integer dancerId);
  //    List<Booking> findByChoreographyId(Integer choreographyId);
  //    Long countByDancerId(Integer dancerId);
  //    Long countByChoreographyId(Integer choreographyId);
  //    Long countByDancerIdAndStatusId(Integer dancerId, Integer statusId);
  //    Long countByChoreographyIdAndStatusId(Integer choreographyId, Integer statusId);
  //    List<Booking> findTop5ByDancerIdAndStatusIdOrderByStartAsc(Integer dancerId, Integer
  // statusId);
  //    List<Booking> findTop5ByChoreographyIdAndStatusIdOrderByStartAsc(Integer choreographyId,
  // Integer statusId);
}
