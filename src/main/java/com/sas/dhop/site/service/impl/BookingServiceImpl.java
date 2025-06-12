package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.BookingService;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.BookingCancelMapper;
import com.sas.dhop.site.util.mapper.BookingMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
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
    private final BookingCancelMapper bookingCancelMapper;
    private final UserService userService;
    private final AreaRepository areaRepository;
    private final DanceTypeService danceTypeService;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final StatusService statusService;
    private final PerformanceRepository performanceRepository;

    // Booking is only for the dancer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForDancer(BookingRequest request) {
        User customer = userService.getLoginUser();

        Dancer dancer = dancerRepository
                .findById(request.dancerId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        DanceType danceType = danceTypeService.findDanceType(request.danceTypeId());

        Area area = areaRepository
                .findById(request.areaId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

        Booking booking = Booking.builder()
                .customer(customer)
                .dancer(dancer)
                .danceType(danceType)
                .area(area)
                .status(statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING))
                .bookingDate(Instant.now())
                .startTime(request.startTime().toLocalTime())
                .endTime(request.endTime().toLocalTime())
                .address(request.address())
                .detail(request.detail())
                .customerPhone(request.customerPhone())
                .dancerPhone(dancer.getUser().getPhone())
                .numberOfTrainingSessions(request.numberOfTrainingSessions())
                .price(request.price())
                .build();

        booking = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(booking);
    }

    // Booking is only for the choreographer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForChoreography(BookingRequest bookingRequest) {
        //        // Validate time
        //        if (bookingRequest.startTime().isAfter(bookingRequest.endTime())) {
        //            throw new BusinessException(ErrorConstant.INVALID_TIME_RANGE);
        //        }

        User customer = userService.getLoginUser();

        Choreography choreography = choreographyRepository
                .findById(bookingRequest.choreographyId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        DanceType danceType = danceTypeService.findDanceType(bookingRequest.danceTypeId());

        Area area = areaRepository
                .findById(customer.getArea().getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

        Booking booking = Booking.builder()
                .customer(customer)
                .dancer(null) // No dancer for choreography booking
                .choreography(choreography)
                .danceType(danceType)
                .area(area)
                .status(statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING))
                .bookingDate(Instant.now())
                .startTime(bookingRequest.startTime().toLocalTime())
                .endTime(bookingRequest.endTime().toLocalTime())
                .address(bookingRequest.address())
                .detail(bookingRequest.detail())
                .customerPhone(bookingRequest.customerPhone())
                .dancerPhone(null)
                .choreographyPhone(choreography.getUser().getPhone())
                .build();

        booking = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(booking);
    }

    // accept button for dancers and choreographer
    @Override
    @Transactional
    public BookingResponse acceptBookingRequest(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
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

    // Change Status when dancer/choreographer press the start working button
    @Override
    public BookingResponse startWork(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
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

    // Change status when dancer/choreographer press the end working button
    @Override
    public BookingResponse endWorking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_IN_PROGRESS)
                && currentStatus.getStatusName().equals(BookingStatus.BOOKING_INACTIVATE))
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_END_WORK);

        Status workingStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_WORKING_DONE);
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

    // TODO: Hàm cancel booking đang thiếu DTO, trong DB thì thiếu reason khi hủy, và thiếu ràng buộc khi hủy.
    @Override
    public BookingCancelResponse cancelBooking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(()-> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        if(booking.getBookingStatus() != BookingStatus.BOOKING_PENDING
        && booking.getBookingStatus() != BookingStatus.BOOKING_WORKING_DONE)
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_CANCEL);

        Status cancelStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_CANCELED);
        booking.setStatus(cancelStatus);

        User user = userService.getLoginUser();

        booking.setCancelReason(booking.getCancelReason());
        booking.setCancelPersonName(user.getName());

        bookingRepository.save(booking);

        return bookingCancelMapper.mapToBookingCancelResponse(booking);
    }

//    @Override
//    public BookingResponse confirmWork(int bookingId) {
//        Booking booking = bookingRepository
//                .findById(bookingId)
//                .orElseThrow(()-> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
//
//        if(booking.getBookingStatus() != BookingStatus.BOOKING_WORKING_DONE)
//            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_END_WORK);
//
//        Status confirmStatus = statusService.findStatusOrCreated(BookingStatus.)
//
//
//
//
//    }


    @Override
    public BookingResponse endBooking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        if (booking.getBookingStatus() != BookingStatus.BOOKING_COMPLETED)
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_COMPLETE);

        Status endStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLETED);
        booking.setStatus(endStatus);
        booking = bookingRepository.save(booking);

        return bookingMapper.mapToBookingResponse(booking);
    }


    public BigDecimal calculateCommissionPrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("200000")) >= 0 && price.compareTo(new BigDecimal("500000")) < 0) {
            // Phí hoa hồng 20%
            return price.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        } else if (price.compareTo(new BigDecimal("500000")) >= 0 && price.compareTo(new BigDecimal("1500000")) < 0) {
            // Phí hoa hồng 15%
            return price.multiply(new BigDecimal("1.15")).setScale(2, RoundingMode.HALF_UP);
        } else if (price.compareTo(new BigDecimal("1500000")) >= 0) {
            // Phí hoa hồng 10%
            return price.multiply(new BigDecimal("1.1")).setScale(2, RoundingMode.HALF_UP);
        }
        // Giá dưới 200k, không tính hoa hồng
        return price.setScale(2, RoundingMode.HALF_UP);
    }

}
