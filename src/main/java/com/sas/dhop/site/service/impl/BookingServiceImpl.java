package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.request.DancerBookingRequest;
import com.sas.dhop.site.dto.request.EndWorkRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.dto.response.MediaResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.mapper.BookingCancelMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j(topic = "[Booking Service]")
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingCancelMapper bookingCancelMapper;
    private final UserService userService;
    private final AreaRepository areaRepository;
    private final DanceTypeService danceTypeService;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final StatusService statusService;
    private final CloudStorageService cloudStorageService;
    private final PerformanceService performanceService;
    private final SubscriptionService subscriptionService;
    private final UserSubscriptionService userSubscriptionService;

    // Booking is only for the dancer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForDancer(DancerBookingRequest request) {
        return BookingResponse.mapToBookingResponse(bookingRepository.save(mapToBooking(request)));
    }

    @Override
    @Transactional
    public BookingResponse createBookingRequestForChoreography(BookingRequest request) {
        Choreography choreography = choreographyRepository
                .findById(request.choreographyId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        log.info("Starting to create booking request for dancer with request: {}", request);

        User customer = userService.getLoginUser();
        log.debug("[Booking for choreography] Fetched logged-in customer: {}", customer.getName());

        DanceType danceType = danceTypeService.findDanceTypeName(request.danceTypeName());
        log.debug("[Booking for choreography] Fetched dance type: {}", danceType.getType());

        Area area = areaRepository.findById(request.areaId()).orElseThrow(() -> {
            log.error("[Booking for choreography] Area not found with id: {}", request.areaId());
            return new BusinessException(ErrorConstant.AREA_NOT_FOUND);
        });
        log.debug("[Booking for choreography] Fetched area: {}", area.getCity());

        Status status = statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING);

        Booking booking = Booking.builder()
                .customer(customer)
                .area(area)
                .choreography(choreography)
                .status(status)
                .danceType(Set.of(danceType))
                .bookingDate(Instant.now())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .address(request.address())
                .detail(request.detail())
                .customerPhone(request.customerPhone())
                .dancerPhone(request.dancerPhone())
                .choreographyPhone(request.choreographyPhone())
                .numberOfTrainingSessions(request.numberOfTrainingSessions())
                .price(calculateCommissionPrice(request.bookingPrice()))
                .build();

        booking = bookingRepository.save(booking);
        log.info("[Booking for choreography] Booking successfully saved with id: {}", booking.getId());

        return BookingResponse.mapToBookingResponse(booking);
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
        UserSubscription subscription = userSubscriptionService.findUserSubscriptionByUser(userService.getLoginUser());

        Integer counted = userSubscriptionService.countBookingFromUserSubscription(subscription);
        if (counted == 0) {
            throw new BusinessException(ErrorConstant.UNCATEGORIZED_ERROR);
        }
        Status activateStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_ACTIVATE);
        booking.setStatus(activateStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Change Status when dancer/choreographer press the start working button
    @Override
    public BookingResponse startWork(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_ACTIVATE)) {
            throw new BusinessException(ErrorConstant.BOOKING_INACTIVATE);
        }

        Status workingStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_IN_PROGRESS);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Change status when dancer/choreographer press the end working button
    @Override
    public BookingResponse endWorking(EndWorkRequest request) {
        Booking booking = bookingRepository
                .findById(request.id())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BookingStatus.BOOKING_IN_PROGRESS)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_END_WORK);
        }

        if (request.multipartFiles() != null) {
            List<MediaResponse> mediaResponses = cloudStorageService.uploadImage(request.multipartFiles());
            for (MediaResponse media : mediaResponses) {
                performanceService.uploadPerformanceForBooking(media.url(), booking);
            }
        }

        Status workingStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_WORKING_DONE);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // This like booking report

    @Override
    public BookingResponse getBookingDetail(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        return BookingResponse.mapToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        return bookingRepository.findAll().stream()
                .map(BookingResponse::mapToBookingResponse)
                .toList();
    }

    @Override
    public BookingCancelResponse cancelBooking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        if (!booking.getBookingStatus().equals(BookingStatus.BOOKING_PENDING)
                && !booking.getBookingStatus().equals(BookingStatus.BOOKING_WORKING_DONE)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_CANCEL);
        }

        Status cancelStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_CANCELED);
        booking.setStatus(cancelStatus);

        User user = userService.getLoginUser();

        booking.setCancelReason(booking.getCancelReason());
        booking.setCancelPersonName(user.getName());

        bookingRepository.save(booking);

        return bookingCancelMapper.mapToBookingCancelResponse(booking);
    }

    // @Override
    // public BookingResponse confirmWork(int bookingId) {
    // Booking booking = bookingRepository
    // .findById(bookingId)
    // .orElseThrow(()-> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
    //
    // if(booking.getBookingStatus() != BookingStatus.BOOKING_WORKING_DONE)
    // throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_END_WORK);
    //
    // Status confirmStatus = statusService.findStatusOrCreated(BookingStatus.)
    //
    //
    //
    //
    // }

    @Override
    public BookingResponse endBooking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        if (!booking.getBookingStatus().equals(BookingStatus.BOOKING_IN_PROGRESS)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_COMPLETE);
        }
        // TODO: chuyen khoan tien truoc khi end booking
        Status endStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_COMPLETED);
        booking.setStatus(endStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse updateBookingInformation(Integer bookingId, BookingRequest bookingRequest) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        User currentUser = userService.getLoginUser();
        if (currentUser.getRoles().equals(RoleName.DANCER)
                && currentUser.getRoles().equals(RoleName.CHOREOGRAPHY)) {
            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
        }

        if (!booking.getBookingStatus().equals(BookingStatus.BOOKING_PENDING)
                && !booking.getBookingStatus().equals(BookingStatus.BOOKING_ACTIVATE))
            throw new BusinessException(ErrorConstant.CAN_NOT_UPDATE_BOOKING);

        booking.setAddress(bookingRequest.address());
        booking.setCustomerPhone(bookingRequest.customerPhone());
        booking.setDetail(bookingRequest.detail());
        booking.setPrice(bookingRequest.bookingPrice());
        booking.setStartTime(bookingRequest.startTime());
        booking.setEndTime(bookingRequest.endTime());
        booking.setNumberOfTrainingSessions(bookingRequest.numberOfTrainingSessions());
        DanceType danceTypeName = danceTypeService.findDanceTypeName(bookingRequest.danceTypeName());
        booking.setDanceType(Set.of(danceTypeName));

        bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Check conflit for dancer schedules
    private void checkDancerBookingConflict(BookingRequest bookingRequest, Dancer dancer) {
        // Lấy tất cả booking của dancer trong ngày
        LocalDate bookingDate = bookingRequest.startTime().toLocalDate();
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getDancer() != null && b.getDancer().getId().equals(dancer.getId()))
                .filter(b -> !b.getStatus().getStatusName().equals(BookingStatus.BOOKING_CANCELED))
                .filter(b -> b.getStartTime().toLocalDate().equals(bookingDate))
                .toList();

        int requestedSessions;
        if (bookingRequest.numberOfTrainingSessions() != null) {
            requestedSessions = bookingRequest.numberOfTrainingSessions();
        } else {
            requestedSessions = 1;
        }

        // Tính tổng số người thuê ở các booking giao nhau
        int totalSessions = requestedSessions;
        for (Booking b : bookings) {
            // Kiểm tra giao nhau thời gian
            boolean isOverlap = !(bookingRequest.endTime().isBefore(b.getStartTime())
                    || bookingRequest.startTime().isAfter(b.getEndTime()));
            if (isOverlap) {
                int bookedSessions = b.getNumberOfTrainingSessions() != null ? b.getNumberOfTrainingSessions() : 1;
                totalSessions += bookedSessions;
            }
        }

        if (totalSessions > dancer.getTeamSize()) {
            throw new BusinessException(ErrorConstant.DANCER_TEAM_SIZE_CONFLICT);
        }
    }

    // Check same time of the schedule
    private void checkChoreographerBookingConflict(BookingRequest bookingRequest, Choreography choreography) {
        LocalDate bookingDate = bookingRequest.startTime().toLocalDate();
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getChoreography() != null
                        && b.getChoreography().getId().equals(choreography.getId()))
                .filter(b -> !b.getStatus().getStatusName().equals(BookingStatus.BOOKING_CANCELED))
                .filter(b -> b.getStartTime().toLocalDate().equals(bookingDate))
                .collect(Collectors.toList());

        for (Booking b : bookings) {
            boolean isOverlap = !(bookingRequest.endTime().isBefore(b.getStartTime())
                    || bookingRequest.startTime().isAfter(b.getEndTime()));
            if (isOverlap) {
                throw new BusinessException(ErrorConstant.CHOOREOGRAPHY_TIME_CONFLICT);
            }
        }
    }

    // Todo: nhớ bỏ hàm này vào xử lý real time luôn
    // Check time need to change status before begin the work (24 hour)
    @Transactional
    public void cancelLateBookingsAutomatically() {
        Instant now = Instant.now();

        // Lọc các booking đang ở trạng thái ACTIVATE và đã qua 24h kể từ startTime
        List<Booking> lateBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus().getStatusName().equals(BookingStatus.BOOKING_ACTIVATE))
                .filter(b -> b.getStartTime().plusHours(24).isBefore(ChronoLocalDateTime.from(now)))
                .collect(Collectors.toList());

        // Lấy trạng thái INACTIVATE để set
        Status inactivateStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_INACTIVATE);

        for (Booking booking : lateBookings) {
            booking.setStatus(inactivateStatus);
            booking.setCancelReason("Tự động chuyển sang INACTIVATE vì đã quá 24h mà không bắt đầu.");
            booking.setCancelPersonName("Hệ thống");

            bookingRepository.save(booking);
        }
    }

    //This like booking report
    @Override
    public BookingCancelResponse bookingComplains(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if(booking.getBookingStatus().equals(BookingStatus.BOOKING_INACTIVATE)
                || booking.getBookingStatus().equals(BookingStatus.BOOKING_PENDING)){
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        Status complainStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_DISPUTED_REQUEST);
        booking.setStatus(complainStatus);

        User currentUser = userService.getLoginUser();
        booking.setCancelPersonName(currentUser.getName());
        booking.setCancelReason(booking.getCancelReason());

        bookingRepository.save(booking);

        return bookingCancelMapper.mapToBookingCancelResponse(booking);
    }

    @Override
    public BookingResponse acceptBookingComplainsProgress(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getBookingStatus().equals(BookingStatus.BOOKING_DISPUTED_REQUEST)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        Status complainStatus = statusService.findStatusOrCreated(BookingStatus.BOOKING_DISPUTED);
        booking.setStatus(complainStatus);
        bookingRepository.save(booking);
        return BookingResponse.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse denyBookingComplainsProgress(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getBookingStatus().equals(BookingStatus.BOOKING_DISPUTED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        // Khôi phục trạng thái trước khi khiếu nại
        Status restoredStatus = booking.getPreviousStatus(); // cần có cột này
        if (restoredStatus == null) {
            throw new BusinessException(ErrorConstant.BOOKING_STATUS_NOT_FOUND);
        }

        booking.setStatus(restoredStatus);
        booking.setPreviousStatus(null); // clear lại nếu cần
        bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Check price after original have commission
    private BigDecimal calculateCommissionPrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("500000")) < 0) {
            throw new BusinessException(ErrorConstant.INVALID_MINIMUM_PRICE);
        }

        if (price.compareTo(new BigDecimal("500000")) >= 0 && price.compareTo(new BigDecimal("1000000")) < 0) {
            // Phí hoa hồng 20%
            return price.multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);
        } else if (price.compareTo(new BigDecimal("1000000")) >= 0 && price.compareTo(new BigDecimal("2000000")) < 0) {
            // Phí hoa hồng 15%
            return price.multiply(new BigDecimal("1.15")).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Phí hoa hồng 10% (>= 2 triệu)
            return price.multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private Booking mapToBooking(DancerBookingRequest request) {

        Dancer dancer = dancerRepository
                .findById(request.dancerId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        User customer = userService.getLoginUser();

        DanceType danceType = danceTypeService.findDanceTypeName(request.danceTypeName());

        Area area = areaRepository
                .findById(request.areaId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

        Status status = statusService.findStatusOrCreated(BookingStatus.BOOKING_PENDING);
        return Booking.builder()
                .startTime(request.startTime())
                .endTime(request.endTime())
                .address(request.address())
                .detail(request.detail())
                .area(area)
                .bookingDate(Instant.now())
                .customer(customer)
                .numberOfTeamMember(request.numberOfTeamMember())
                .danceType(Set.of(danceType))
                .dancer(dancer)
                .status(status)
                .customerPhone(request.customerPhone())
                .dancerPhone(request.dancerPhone())
                .price(calculateCommissionPrice(request.bookingPrice()))
                .build();
    }
}
