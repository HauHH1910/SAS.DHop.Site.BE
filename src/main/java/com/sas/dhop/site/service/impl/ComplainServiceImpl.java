package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.BookingStatus.*;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.dto.request.ComplainRequest;
import com.sas.dhop.site.dto.response.ComplainResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.Complain;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.repository.ComplainRepository;
import com.sas.dhop.site.service.ComplainService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.ComplainMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j(topic = "[Complain Service]")
public class ComplainServiceImpl implements ComplainService {

    private final StatusService statusService;
    private final BookingRepository bookingRepository;
    private final ComplainRepository complainRepository;
    private final UserService userService;
    private final ComplainMapper complainMapper;

    @Override
    public ComplainResponse bookingComplains(ComplainRequest complainRequest) {

        Booking booking = bookingRepository
                .findById(complainRequest.bookingId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (BOOKING_INACTIVATE.equals(booking.getBookingStatus())
                || BOOKING_PENDING.equals(booking.getBookingStatus())) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        User currentUser = userService.getLoginUser();
        if (!currentUser.equals(booking.getCustomer())
                && (booking.getDancer() != null
                        && !currentUser.equals(booking.getDancer().getUser()))
                && (booking.getChoreography() != null
                        && !currentUser.equals(booking.getChoreography().getUser()))) {
            throw new BusinessException(ErrorConstant.INVALID_USER);
        }

        // Lưu trạng thái hiện tại của booking trước khi thay đổi
        Status previousStatus = booking.getStatus();

        // Create new complain
        Complain complain = Complain.builder()
                .booking(booking)
                .cancelPersonName(currentUser.getName())
                .content(complainRequest.content())
                .status(statusService.findStatusOrCreated(BOOKING_DISPUTED_REQUEST))
                .previousStatus(previousStatus) // Lưu trạng thái trước đó
                .user(currentUser)
                .build();

        complainRepository.save(complain);

        Status complainStatus = statusService.findStatusOrCreated(BOOKING_DISPUTED_REQUEST);
        booking.setStatus(complainStatus);
        bookingRepository.save(booking);

        return complainMapper.mapToComplain(complain);
    }

    @Override
    public ComplainResponse cancelBookingComplains(Integer bookingId) {
        // Find booking
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        // Check if booking is in DISPUTED status
        if (!booking.getStatus().getStatusName().equals(BookingStatus.BOOKING_DISPUTED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        // Find complain
        Complain complain = complainRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.COMPLAIN_NOT_FOUND));

        // Khôi phục lại trạng thái trước đó của booking
        if (complain.getPreviousStatus() != null) {
            booking.setStatus(complain.getPreviousStatus());
        } else {
            // Nếu không có trạng thái trước đó, set về trạng thái mặc định
            booking.setStatus(statusService.findStatusOrCreated(BookingStatus.BOOKING_CANCELED));
        }

        // Update complain status
        complain.setStatus(statusService.findStatusOrCreated(BOOKING_COMPLAIN_CANCELED));
        complainRepository.save(complain);

        // Save booking with restored status
        bookingRepository.save(booking);

        return complainMapper.mapToComplain(complain);
    }

    @Override
    public ComplainResponse approveBookingComplains(Integer bookingId) {
        // Find booking
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        // Check if booking is in DISPUTED status
        log.info(booking.getStatus().getStatusName());

        if (!booking.getStatus().getStatusName().equals(BOOKING_DISPUTED_REQUEST)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        // Find complain
        Complain complain = complainRepository
                .findByBooking(booking)
                .orElseThrow(() -> new BusinessException(ErrorConstant.COMPLAIN_NOT_FOUND));

        //        // Khôi phục lại trạng thái trước đó của booking
        //        if (complain.getPreviousStatus() != null) {
        //            booking.setStatus(complain.getPreviousStatus());
        //        } else {
        //            // Nếu không có trạng thái trước đó, set về trạng thái mặc định
        //            booking.setStatus(statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLETED));
        //        }

        // Update complain status
        complain.setStatus(statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLAIN_APPROVED));
        complainRepository.save(complain);

        // Save booking with restored status
        bookingRepository.save(booking);

        return complainMapper.mapToComplain(complain);
    }

    @Override
    public ComplainResponse completeBookingComplains(Integer bookingId) {
        // Find booking
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        // Check if booking is in DISPUTED status
        log.info(booking.getStatus().getStatusName());

        if (!booking.getStatus().getStatusName().equals(BOOKING_COMPLAIN_APPROVED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        // Find complain
        Complain complain = complainRepository
                .findByBooking(booking)
                .orElseThrow(() -> new BusinessException(ErrorConstant.COMPLAIN_NOT_FOUND));

        //        // Khôi phục lại trạng thái trước đó của booking
        //        if (complain.getPreviousStatus() != null) {
        //            booking.setStatus(complain.getPreviousStatus());
        //        } else {
        //            // Nếu không có trạng thái trước đó, set về trạng thái mặc định
        //            booking.setStatus(statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLETED));
        //        }

        // Update complain status
        complain.setStatus(statusService.findStatusOrCreated(BOOKING_COMPLAIN_COMPLETED));
        complainRepository.save(complain);

        // Save booking with restored status
        bookingRepository.save(booking);

        return complainMapper.mapToComplain(complain);
    }
}
