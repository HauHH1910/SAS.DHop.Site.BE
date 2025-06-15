package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.BookingFeedbackRequest;
import com.sas.dhop.site.dto.response.BookingFeedbackResponse;
import java.util.List;

public interface BookingFeedbackService {

  List<BookingFeedbackResponse> getFeedbackByDancerId(Integer dancerId);

  List<BookingFeedbackResponse> getFeedbackByChoreographer(Integer choreographerId);

  BookingFeedbackResponse createBookingFeedback(BookingFeedbackRequest bookingFeebackRequest);

  BookingFeedbackResponse getBookingFeedbackByBookingId(Integer bookingId);
}
