package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.dto.request.BookingFeedbackRequest;
import com.sas.dhop.site.dto.response.BookingFeedbackResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.BookingFeedbackRepository;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.DancerRepository;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.mapper.BookingFeebackMapper;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j(topic = "[Booking Feedback Service]")
public class BookingFeedbackServiceImpl implements BookingFeedbackService {
    private final UserService userService;
    private final StatusService statusService;
    private final BookingRepository bookingRepository;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final BookingFeedbackRepository bookingFeedbackRepository;
    private final BookingFeebackMapper bookingFeebackMapper;

    @Override
    public List<BookingFeedbackResponse> getFeedbackByDancerId(Integer dancerId) {
        Dancer dancer = dancerRepository
                .findById(dancerId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        List<BookingFeedback> feedbacks = bookingFeedbackRepository.findAll().stream()
                .filter(feedback ->
                        feedback.getToUser().getId().equals(dancer.getUser().getId()))
                .toList();

        return feedbacks.stream()
                .map(bookingFeebackMapper::mapToFeedbackResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingFeedbackResponse> getFeedbackByChoreographer(Integer choreographerId) {
        Choreography choreography = choreographyRepository
                .findById(choreographerId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        List<BookingFeedback> feedbacks = bookingFeedbackRepository.findAll().stream()
                .filter(feedback -> feedback.getToUser()
                        .getId()
                        .equals(choreography.getUser().getId()))
                .toList();

        return feedbacks.stream()
                .map(bookingFeebackMapper::mapToFeedbackResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingFeedbackResponse createBookingFeedback(BookingFeedbackRequest bookingFeebackRequest) {
        Booking booking = bookingRepository
                .findById(bookingFeebackRequest.bookingId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getStatus().getStatusName().equals(BookingStatus.BOOKING_COMPLETED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_FEEDBACK);
        }
        // Take customer
        User fromUser = userService.getLoginUser();

        User toUser;
        if (booking.getDancer() != null) {
            toUser = booking.getDancer().getUser();
        } else if (booking.getChoreography() != null) {
            toUser = booking.getChoreography().getUser();
        } else {
            throw new BusinessException(ErrorConstant.INVALID_BOOKING);
        }

        // Create and save the feedback
        BookingFeedback bookingFeedback = BookingFeedback.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .rating(bookingFeebackRequest.rating())
                .comment(bookingFeebackRequest.comment())
                .status(statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLETED))
                .booking(booking)
                .build();

        bookingFeedback = bookingFeedbackRepository.save(bookingFeedback);

        return bookingFeebackMapper.mapToFeedbackResponse(bookingFeedback);
    }

    @Override
    public BookingFeedbackResponse getBookingFeedbackByBookingId(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getStatus().getStatusName().equals(BookingStatus.BOOKING_ACCEPTED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_FEEDBACK);
        }

        bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        BookingFeedback feedback = bookingFeedbackRepository.findAll().stream()
                .filter(f -> f.getBooking().getId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        return bookingFeebackMapper.mapToFeedbackResponse(feedback);
    }
}
