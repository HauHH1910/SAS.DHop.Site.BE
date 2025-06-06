package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.AreaRepository;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.repository.DancerRepository;
import com.sas.dhop.site.service.BookingService;
import com.sas.dhop.site.service.DanceTypeService;
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
    private final DanceTypeService danceTypeService;
    private final DancerRepository dancerRepository;

    @Override
    public BookingResponse createBookingRequestForDancer(BookingRequest bookingRequest) {
        //        User currentUser = userService.getLoginUser();
        //
        //        boolean hasRole = currentUser.getRoles().stream().anyMatch(
        //                role -> role.getName().equals(RoleName.USER)
        //        );
        //        if (!hasRole)
        //            throw new BusinessException(ErrorConstant.USER_NOT_FOUND);
        //
        //        if(bookingRequest.dancerId() == null && bookingRequest.choreographyId() != null)
        //            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
        //
        //        Dancer hiredPerson = Da.findUserById(bookingRequest.dancerId());
        //        boolean isDancer = hiredPerson.getRoles().stream().anyMatch(role ->
        // role.getName().equals(RoleName.DANCER));
        //
        //        if(!isDancer)
        //            throw new BusinessException(ErrorConstant.NOT_DANCER);
        //
        //        //Create New Booking for dancer
        //        Booking booking = new Booking();
        //        booking.setCustomer(currentUser);
        //        booking.setDancer(hiredPerson);

        return null;
    }

    @Override
    public BookingResponse createBookingRequestForChoreography(BookingRequest bookingRequest) {
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
