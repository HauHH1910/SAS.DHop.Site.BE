package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.AreaRepository;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.DancerRepository;
import com.sas.dhop.site.service.BookingService;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.BookingMapper;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ChoreographyRepository choreographyRepository;
    private final StatusService statusService;

    //Booking is only for the dancer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForDancer(BookingRequest bookingRequest) {
        User customer = userService.getLoginUser();
        
        Dancer dancer = dancerRepository.findById(bookingRequest.dancerId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));
        
        DanceType danceType = danceTypeService.findDanceType(bookingRequest.danceTypeId());
        
        Area area = areaRepository.findById(customer.getArea().getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));
        
        Booking booking = Booking.builder()
                .customer(customer)
                .dancer(dancer)
                .choreography(null) // No choreography for dancer booking
                .danceType(danceType)
                .area(area)
                .status(statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING))
                .bookingDate(Instant.now())
                .startTime(bookingRequest.startDay())
                .endTime(bookingRequest.endTime())
                .address(bookingRequest.address())
                .detail(bookingRequest.detail())
                .customerPhone(bookingRequest.customerPhone())
                .dancerPhone(dancer.getUser().getPhone())
                .choreographyPhone(null)
                .build();
        
        booking = bookingRepository.save(booking);
        
        return bookingMapper.mapToBookingResponse(booking);
    }

    //Booking is only for the choreographer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForChoreography(BookingRequest bookingRequest) {
        User customer = userService.getLoginUser();
        
        Choreography choreography = choreographyRepository.findById(bookingRequest.choreographyId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));
        
        DanceType danceType = danceTypeService.findDanceType(bookingRequest.danceTypeId());
        
        Area area = areaRepository.findById(customer.getArea().getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));
        
        Booking booking = Booking.builder()
                .customer(customer)
                .dancer(null) // No dancer for choreography booking
                .choreography(choreography)
                .danceType(danceType)
                .area(area)
                .status(statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING))
                .bookingDate(Instant.now())
                .startTime(bookingRequest.startDay())
                .endTime(bookingRequest.endTime())
                .address(bookingRequest.address())
                .detail(bookingRequest.detail())
                .customerPhone(bookingRequest.customerPhone())
                .dancerPhone(null)
                .choreographyPhone(choreography.getUser().getPhone())
                .build();
        
        booking = bookingRepository.save(booking);
        
        return bookingMapper.mapToBookingResponse(booking);
    }

    //accept button for dancers and choreographer
    @Override
    @Transactional
    public BookingResponse acceptBookingRequest(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_PENDING)) {
            throw new BusinessException(ErrorConstant.BOOKING_NOT_ACCEPTABLE);
        }

        Status activateStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_ACTIVATE);
        booking.setStatus(activateStatus);
        booking = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(booking);
    }

    //Change Status when dancer/choreographer press the start working button
    @Override
    public BookingResponse startWork(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_ACTIVATE)
        && currentStatus.getStatusName().equals(BookingStatus.BOOKING_INACTIVATE)) {
            throw new BusinessException(ErrorConstant.BOOKING_INACTIVATE);
        }

        Status workingStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_IN_PROGRESS);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(booking);

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

    @Override
    public BookingResponse cancelBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_ACTIVATE)
                && currentStatus.getStatusName().equals(BookingStatus.BOOKING_INACTIVATE)) {
            throw new BusinessException(ErrorConstant.BOOKING_INACTIVATE);
        }

        return null;
    }

    @Override
    public BookingResponse endBooking(int bookingId) {
        return null;
    }


}
