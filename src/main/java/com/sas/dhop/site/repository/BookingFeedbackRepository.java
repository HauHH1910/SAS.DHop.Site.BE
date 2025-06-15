package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.BookingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingFeedbackRepository
		extends
			JpaRepository<BookingFeedback, Integer>,
			JpaSpecificationExecutor<BookingFeedback> {
}
