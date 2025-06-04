package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.repository.AreaRepository;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.service.BookingService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.BookingMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j(topic = "[Booking Service]")
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final AreaRepository areaRepository;

    @Override
    public BookingResponse createBookingRequest(BookingRequest bookingRequest) {
        return null;
    }

    @Override
    public BookingResponse acceptBookingRequest(int bookingId) {
        return null;
    }

    @Override
    public BookingResponse startWork(int bookingId) {
        return null;
    }

    @Override
    public BookingResponse getBookingDetail(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        return bookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::mapToBookingResponse)
                .toList();
    }
}
