package com.sas.dhop.site.util.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
	// @Mapping(target = "dancer", source = "dancer.dancerNickName")
	// @Mapping(target = "status", source = "status.statusName")
	// @Mapping(target = "area", expression =
	// "java(com.sas.dhop.site.dto.response.AreaResponse.mapToAreaResponse(booking.getArea()))")
	// BookingResponse toBookingResponse(Booking booking);
	//
	// List<BookingResponse> toBookingResponseList(List<Booking> bookings);
}
