package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.BookingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookingFeedbackRepository
        extends JpaRepository<BookingFeedback, Integer>, JpaSpecificationExecutor<BookingFeedback> {
    Optional<BookingFeedback> findByBooking(Booking booking);
}
