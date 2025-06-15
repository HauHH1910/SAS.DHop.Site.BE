package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.BookingFeedbackResponse;
import com.sas.dhop.site.model.BookingFeedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingFeebackMapper {
  BookingFeedbackResponse mapToFeedbackResponse(BookingFeedback bookingFeedback);
}
