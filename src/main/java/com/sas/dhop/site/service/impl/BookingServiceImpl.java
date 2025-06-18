package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.BookingStatus.*;

import com.sas.dhop.site.constant.PaymentStatus;
import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.dto.request.BookingRequest;
import com.sas.dhop.site.dto.request.DancerAcceptRequest;
import com.sas.dhop.site.dto.request.DancerBookingRequest;
import com.sas.dhop.site.dto.request.EndWorkRequest;
import com.sas.dhop.site.dto.response.BookingCancelResponse;
import com.sas.dhop.site.dto.response.BookingResponse;
import com.sas.dhop.site.dto.response.MediaResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.BookingStatus;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.mapper.BookingCancelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final AuthenticationService authenticationService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    // Booking is only for the dancer, the booker wants
    @Override
    @Transactional
    public BookingResponse createBookingRequestForDancer(DancerBookingRequest request) {
        checkDancerBookingConflict(request);
        return BookingResponse.mapToBookingResponse(bookingRepository.save(buildDancerBooking(request)));
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

        Status status = statusService.findStatusOrCreated(BOOKING_PENDING);

        Booking booking = Booking.builder()
                .customer(customer)
                .area(area)
                .choreography(choreography)
                .status(status)
                .danceType(Set.of(danceType))
                .bookingDate(LocalDateTime.now())
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
    public BookingResponse acceptBookingRequest(int bookingId, DancerAcceptRequest request) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BOOKING_PENDING)) {
            throw new BusinessException(ErrorConstant.BOOKING_NOT_ACCEPTABLE);
        }

        Status activateStatus = statusService.findStatusOrCreated(BOOKING_ACTIVATE);
        booking.setDancerAccountNumber(request.accountNumber());
        booking.setDancerPhone(request.dancerPhone());
        booking.setDancerBank(request.bank());
        booking.setStatus(activateStatus);
        booking = bookingRepository.save(booking);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
//        String currentTimeString = booking.getBookingDate().format(formatter);
//
//        long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
//        log.info("[order code] - [{}]", orderCode);
//        paymentRepository.save(new Payment(
//                orderCode, PaymentStatus.NOT_PAID, booking.getPrice().intValue()));
        return BookingResponse.mapToBookingResponse(booking);
    }

    // Change Status when dancer/choreographer press the start working button
    @Override
    public BookingResponse startWork(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BOOKING_ACTIVATE)) {
            throw new BusinessException(ErrorConstant.BOOKING_INACTIVATE);
        }

        Status workingStatus = statusService.findStatusOrCreated(BOOKING_IN_PROGRESS);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Change status when dancer/choreographer press the end working button
    @Override
    public BookingResponse endWorking(EndWorkRequest request) {
        Booking booking = bookingRepository
                .findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        Status currentStatus = booking.getStatus();
        if (!currentStatus.getStatusName().equals(BOOKING_IN_PROGRESS)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_END_WORK);
        }

        if (request.getMultipartFiles() != null) {
            List<MediaResponse> mediaResponses = cloudStorageService.uploadImage(request.getMultipartFiles());
            for (MediaResponse media : mediaResponses) {
                performanceService.uploadPerformanceForBooking(media.url(), booking);
            }
        }

        Status workingStatus = statusService.findStatusOrCreated(BOOKING_WORKING_DONE);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }


    @Override
    public BookingResponse userSentPayment(EndWorkRequest request) {
        Booking booking = bookingRepository
                .findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        List<MediaResponse> mediaResponses = cloudStorageService.uploadImage(request.getMultipartFiles());
        for (MediaResponse media : mediaResponses) {
            performanceService.uploadPerformanceForBooking(media.url(), booking);
        }

        Status workingStatus = statusService.findStatusOrCreated(BOOKING_WORKING_DONE);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

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
        if (!booking.getBookingStatus().equals(BOOKING_PENDING)
                && !booking.getBookingStatus().equals(BOOKING_WORKING_DONE)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_CANCEL);
        }

        Status cancelStatus = statusService.findStatusOrCreated(BOOKING_CANCELED);
        booking.setStatus(cancelStatus);

        bookingRepository.save(booking);

        return bookingCancelMapper.mapToBookingCancelResponse(booking);
    }


    @Override
    public BookingResponse completeWork(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getStatus().getStatusName().equals(BOOKING_WORKING_DONE)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_COMPLETE);
        }

        booking.setStatus(statusService.findStatusOrCreated(BOOKING_COMPLETED));

        return BookingResponse.mapToBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse endBooking(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        if (!booking.getBookingStatus().equals(BOOKING_IN_PROGRESS)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_COMPLETE);
        }

        Status endStatus = statusService.findStatusOrCreated(BOOKING_COMPLETED);
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

        if (!booking.getBookingStatus().equals(BOOKING_PENDING)
                && !booking.getBookingStatus().equals(BOOKING_ACTIVATE))
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

    @Override
    public List<BookingResponse> findBookingByAuthenticatedUser() {
        boolean isUser = authenticationService.authenticationChecking(RolePrefix.USER_PREFIX);
        boolean isDancer = authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX);
        boolean isChoreography = authenticationService.authenticationChecking(RolePrefix.CHOREOGRAPHY_PREFIX);

        User user = userService.getLoginUser();

        if (isUser) {
            return bookingRepository.findAllByCustomer(user).stream()
                    .map(BookingResponse::mapToBookingResponse)
                    .toList();
        } else if (isDancer) {
            Dancer dancer = dancerRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_DANCER));
            return bookingRepository.findAllByDancer(dancer).stream()
                    .map(BookingResponse::mapToBookingResponse)
                    .toList();
        } else if (isChoreography) {
            Choreography choreography = choreographyRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_FOUND_CHOREOGRAPHY));
            return bookingRepository.findAllByChoreography(choreography).stream()
                    .map(BookingResponse::mapToBookingResponse)
                    .toList();
        } else {
            return bookingRepository.findAll().stream()
                    .map(BookingResponse::mapToBookingResponse)
                    .toList();
        }
    }

    // This like booking report
    @Override
    public BookingCancelResponse bookingComplains(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (booking.getBookingStatus().equals(BOOKING_INACTIVATE)
                || booking.getBookingStatus().equals(BOOKING_PENDING)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        Status complainStatus = statusService.findStatusOrCreated(BOOKING_DISPUTED_REQUEST);
        booking.setStatus(complainStatus);

        User currentUser = userService.getLoginUser();

        bookingRepository.save(booking);

        return bookingCancelMapper.mapToBookingCancelResponse(booking);
    }

    @Override
    public BookingResponse acceptBookingComplainsProgress(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getBookingStatus().equals(BOOKING_DISPUTED_REQUEST)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        Status complainStatus = statusService.findStatusOrCreated(BOOKING_DISPUTED);
        booking.setStatus(complainStatus);
        bookingRepository.save(booking);
        return BookingResponse.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse denyBookingComplainsProgress(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getBookingStatus().equals(BOOKING_DISPUTED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        //        // Khôi phục trạng thái trước khi khiếu nại
        ////        Status restoredStatus = booking.getPreviousStatus(); // cần có cột này
        //        if (restoredStatus == null) {
        //            throw new BusinessException(ErrorConstant.BOOKING_STATUS_NOT_FOUND);
        //        }
        //
        //        booking.setStatus(restoredStatus);
        //        booking.setPreviousStatus(null); // clear lại nếu cần
        bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking);
    }

    // Check price after original have commission
    private BigDecimal calculateCommissionPrice(BigDecimal price) {
//        if (price.compareTo(new BigDecimal("500000")) < 0) {
//            throw new BusinessException(ErrorConstant.INVALID_MINIMUM_PRICE);
//        }

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

    private Booking buildDancerBooking(DancerBookingRequest request) {
        Dancer dancer = dancerRepository
                .findById(request.dancerId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        User customer = userService.getLoginUser();

        List<String> listDanceType = request.danceTypeName();

        Set<DanceType> danceTypes = new HashSet<>();

        for (String type : listDanceType) {
            DanceType danceType = danceTypeService.findDanceTypeName(type);
            danceTypes.add(danceType);
        }

        Area area = areaRepository
                .findById(request.areaId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

        Status status = statusService.findStatusOrCreated(BOOKING_PENDING);
        return Booking.builder()
                .startTime(LocalDateTime.now())
                .endTime(request.endTime())
                .address(request.address())
                .detail(request.detail())
                .area(area)
                .bookingDate(request.bookingDate())
                .customer(customer)
                .numberOfTeamMember(request.numberOfTeamMember())
                .danceType(danceTypes)
                .dancer(dancer)
                .status(status)
                .customerPhone(request.customerPhone())
                .dancerPhone(request.dancerPhone())
                .price(calculateCommissionPrice(request.bookingPrice()))
                .build();
    }

    private void checkDancerBookingConflict(DancerBookingRequest request) {
        boolean isDancer = authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX);
        if (isDancer) {
            throw new BusinessException(ErrorConstant.DANCER_CAN_NOT_BOOKING);
        }
        bookingRepository.findByBookingDate(request.bookingDate()).ifPresent(booking -> {
            if (Arrays.asList(BOOKING_ACTIVATE, BOOKING_IN_PROGRESS, BOOKING_WORKING_DONE)
                    .contains(booking.getStatus().getStatusName())) {
                int availableTeamMembers = booking.getDancer().getTeamSize() - booking.getNumberOfTeamMember();

                if (availableTeamMembers < request.numberOfTeamMember()) {
                    throw new BusinessException(ErrorConstant.BOOKING_NUMBER_OF_TEAM_MEMBER);
                }
            }
        });
    }

    // Check same time of the schedule
    private void checkChoreographerBookingConflict(BookingRequest bookingRequest, Choreography choreography) {
        LocalDate bookingDate = bookingRequest.startTime().toLocalDate();
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getChoreography() != null
                        && b.getChoreography().getId().equals(choreography.getId()))
                .filter(b -> !b.getStatus().getStatusName().equals(BOOKING_CANCELED))
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
                .filter(b -> b.getStatus().getStatusName().equals(BOOKING_ACTIVATE))
                .filter(b -> b.getStartTime().plusHours(24).isBefore(ChronoLocalDateTime.from(now)))
                .collect(Collectors.toList());

        // Lấy trạng thái INACTIVATE để set
        Status inactivateStatus = statusService.findStatusOrCreated(BOOKING_INACTIVATE);

        for (Booking booking : lateBookings) {
            booking.setStatus(inactivateStatus);
            //            booking.setCancelReason("Tự động chuyển sang INACTIVATE vì đã quá 24h mà không bắt đầu.");
            //            booking.setCancelPersonName("Hệ thống");

            bookingRepository.save(booking);
        }
    }
}
