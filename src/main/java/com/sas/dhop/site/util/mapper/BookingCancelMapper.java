package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingCancelMapper {
	BookingCancelResponse mapToBookingCancelResponse(Booking booking);
}
