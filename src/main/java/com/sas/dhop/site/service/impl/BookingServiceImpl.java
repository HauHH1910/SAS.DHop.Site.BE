package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.BookingStatus.*;

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
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.mapper.BookingCancelMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
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
    private final UserSubscriptionService userSubscriptionService;

    //TODO: Hiện tại đang thiếu các hàm liên quan tới luồng booking không chỉ định,
    // cần làm các hàm liên quan tới apply danh sách và get danh sach đó

    // Booking is only for the dancer, the booker wants
    @Override
    @Transactional
    public List<BookingResponse> createBookingRequestForDancer(DancerBookingRequest request) {
        boolean conflict = checkDancerBookingConflict(request);

        if (!conflict) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_START);
        }

        return List.of(BookingResponse.mapToBookingResponse(
                bookingRepository.save(buildDancerBooking(request)), new ArrayList<>()));
    }

    // TODO: Cần chỉnh sửa service liên quan đến tạo booking request của biên đạo,
    // xử lý các xung đột về mặt thời gian có trùng nhau hay không,
    // hàm để xử lý thông tin tiến độ làm việc trong phần chi tiết của booking dối với biên đạo.
    @Override
    @Transactional
    public BookingResponse createBookingRequestForChoreography(BookingRequest request) {
        Choreography choreography = choreographyRepository
                .findById(request.choreographyId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        cancelLateBookingsAutomatically();
        log.info("Starting to create booking request for dancer with request: {}", request);

        checkChoreographerBookingConflict(request, choreography);

        User customer = userService.getLoginUser();
        log.debug("[Booking for choreography] Fetched logged-in customer: {}", customer.getName());

        DanceType danceType = danceTypeService.findDanceTypeName(request.danceTypeName());
        log.debug("[Booking for choreography] \nFetched dance type: {}", danceType.getType());

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

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

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

        User user = userService.getLoginUser();

        boolean hasRole = authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX)
                || authenticationService.authenticationChecking(RolePrefix.CHOREOGRAPHY_PREFIX);

        if (hasRole) {
            try {
                userSubscriptionService.addOrForceToBuySubscription(user.getId());
            } catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
                log.warn("Found duplicate records when checking subscription. Using default behavior.");
            }
        }

        Status activateStatus = statusService.findStatusOrCreated(BOOKING_ACTIVATE);
        booking.setDancerAccountNumber(request.accountNumber());
        booking.setDancerPhone(request.dancerPhone());
        booking.setDancerBank(request.bank());
        booking.setStatus(activateStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
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

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
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

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    @Transactional
    public BookingResponse userSentPayment(EndWorkRequest request) {
        Booking booking = bookingRepository
                .findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        List<MediaResponse> mediaResponses = cloudStorageService.uploadImage(request.getMultipartFiles());
        for (MediaResponse media : mediaResponses) {
            performanceService.uploadPerformanceForBooking(media.url(), booking);
        }

        Status workingStatus = statusService.findStatusOrCreated(BOOKING_SENT_IMAGE);
        booking.setStatus(workingStatus);
        booking = bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    public BookingResponse getBookingDetail(int bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));
        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        return bookingRepository.findAll().stream()
                .map(booking -> BookingResponse.mapToBookingResponse(booking, new ArrayList<>()))
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
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getStatus().getStatusName().equals(BOOKING_WORKING_DONE)) {
            throw new BusinessException(ErrorConstant.BOOKING_CAN_NOT_COMPLETE);
        }

        booking.setStatus(statusService.findStatusOrCreated(BOOKING_COMPLETED));

        return BookingResponse.mapToBookingResponse(bookingRepository.save(booking), new ArrayList<>());
    }

    @Override
    public BookingResponse dancerAcceptBooking(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getStatus().getStatusName().equals(BOOKING_SENT_IMAGE)) {
            throw new BusinessException(ErrorConstant.BOOKING_NOT_PAY);
        }

        booking.setStatus(statusService.findStatusOrCreated(BOOKING_ACCEPTED));

        return BookingResponse.mapToBookingResponse(bookingRepository.save(booking), new ArrayList<>());
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

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    public BookingResponse updateBookingInformation(Integer bookingId, BookingRequest bookingRequest) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        User currentUser = userService.getLoginUser();

        boolean hasRole = currentUser.getRoles().stream()
                .anyMatch(role ->
                        Arrays.asList(RoleName.DANCER, RoleName.CHOREOGRAPHY).contains(role.getName()));

        if (hasRole) {
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

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    public List<BookingResponse> findBookingByAuthenticatedUser() {
        boolean isUser = authenticationService.authenticationChecking(RolePrefix.USER_PREFIX);
        boolean isDancer = authenticationService.authenticationChecking(RolePrefix.DANCER_PREFIX);
        boolean isChoreography = authenticationService.authenticationChecking(RolePrefix.CHOREOGRAPHY_PREFIX);

        User user = userService.getLoginUser();

        if (isUser) {
            return bookingRepository.findAllByCustomer(user).stream()
                    .map(booking -> BookingResponse.mapToBookingResponse(
                            booking, performanceService.getPerformanceByBookingId(booking.getId())))
                    .toList();
        } else if (isDancer) {
            Dancer dancer = dancerRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_DANCER));
            return bookingRepository.findAllByDancer(dancer).stream()
                    .map(booking -> BookingResponse.mapToBookingResponse(
                            booking, performanceService.getPerformanceByBookingId(booking.getId())))
                    .toList();
        } else if (isChoreography) {
            Choreography choreography = choreographyRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_FOUND_CHOREOGRAPHY));
            return bookingRepository.findAllByChoreography(choreography).stream()
                    .map(booking -> BookingResponse.mapToBookingResponse(
                            booking, performanceService.getPerformanceByBookingId(booking.getId())))
                    .toList();
        } else {
            return bookingRepository.findAll().stream()
                    .map(booking -> BookingResponse.mapToBookingResponse(booking, new ArrayList<>()))
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
        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    @Override
    public BookingResponse denyBookingComplainsProgress(Integer bookingId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.BOOKING_NOT_FOUND));

        if (!booking.getBookingStatus().equals(BOOKING_DISPUTED)) {
            throw new BusinessException(ErrorConstant.CAN_NOT_COMPLAIN);
        }

        bookingRepository.save(booking);

        return BookingResponse.mapToBookingResponse(booking, new ArrayList<>());
    }

    // Check price after original have commission
    private BigDecimal calculateCommissionPrice(BigDecimal price) {
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

        Set<DanceType> danceTypes = request.danceTypeName().stream()
                .distinct()
                .map(danceTypeService::findDanceTypeName)
                .collect(Collectors.toSet());

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

    private boolean checkDancerBookingConflict(DancerBookingRequest request) {
        bookingRepository.findByBookingDate(request.bookingDate()).ifPresent(booking -> {
            if (Arrays.asList(BOOKING_ACTIVATE, BOOKING_IN_PROGRESS, BOOKING_WORKING_DONE)
                    .contains(booking.getStatus().getStatusName())) {
                int availableTeamMembers = booking.getDancer().getTeamSize() - booking.getNumberOfTeamMember();

                if (availableTeamMembers < request.numberOfTeamMember()) {
                    throw new BusinessException(ErrorConstant.BOOKING_NUMBER_OF_TEAM_MEMBER);
                }
            }
        });
        return true;
    }

    // Check same time of the schedule
    private void checkChoreographerBookingConflict(BookingRequest bookingRequest, Choreography choreography) {
        LocalDate bookingDate = bookingRequest.startTime().toLocalDate();
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getChoreography() != null
                        && b.getChoreography().getId().equals(choreography.getId()))
                .filter(b -> !b.getStatus().getStatusName().equals(BOOKING_CANCELED))
                .filter(b -> b.getStartTime().toLocalDate().equals(bookingDate))
                .toList();

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
                .toList();

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
