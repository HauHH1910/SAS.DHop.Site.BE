package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse mapToBookingResponse(Booking booking);
}
