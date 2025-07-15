package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByDancer(Dancer dancer);

    List<Booking> findAllByChoreography(Choreography choreography);

    List<Booking> findAllByCustomer(User customer);

    Optional<Booking> findByBookingDate(LocalDateTime bookingDate);

    @Query(
            "select count(b) from Booking b where b.dancer = ?1 and b.bookingDate between ?2 and ?3 and b.status.statusName = 'Đã hoàn tất'")
    long countBookingByDancerAndBookingDateBetween(
            Dancer dancer, LocalDateTime bookingDateAfter, LocalDateTime bookingDateBefore);

    List<Booking> findAllByStatus(Optional<Status> status);

    @Query("SELECT CAST(b.bookingDate AS date) as bookingDate, COUNT(b) as count " + "FROM Booking b "
            + "WHERE b.bookingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY CAST(b.bookingDate AS date)")
    List<Object[]> countBookingsByDateRange(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT CAST(b.bookingDate AS date) as bookingDate, SUM(b.price) as revenue " + "FROM Booking b "
            + "WHERE b.bookingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY CAST(b.bookingDate AS date)")
    List<Object[]> sumRevenueByDateRange(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE_FORMAT(b.bookingDate, '%Y-%m') as yearMonth, COUNT(b) as count " + "FROM Booking b "
            + "WHERE YEAR(b.bookingDate) = :year "
            + "GROUP BY DATE_FORMAT(b.bookingDate, '%Y-%m')")
    List<Object[]> countBookingsByMonthInYear(@Param("year") int year);

    @Query("SELECT DATE_FORMAT(b.bookingDate, '%Y-%m') as yearMonth, SUM(b.price) as revenue " + "FROM Booking b "
            + "WHERE YEAR(b.bookingDate) = :year "
            + "GROUP BY DATE_FORMAT(b.bookingDate, '%Y-%m')")
    List<Object[]> sumRevenueByMonthInYear(@Param("year") int year);

    @Query("SELECT DATE_FORMAT(b.bookingDate, '%Y-%m') as yearMonth, COUNT(b) as count " + "FROM Booking b "
            + "WHERE b.bookingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY DATE_FORMAT(b.bookingDate, '%Y-%m')")
    List<Object[]> countBookingsByDateRangeGroupByMonth(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE_FORMAT(b.bookingDate, '%Y-%m') as yearMonth, SUM(b.price) as revenue " + "FROM Booking b "
            + "WHERE b.bookingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY DATE_FORMAT(b.bookingDate, '%Y-%m')")
    List<Object[]> sumRevenueByDateRangeGroupByMonth(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
