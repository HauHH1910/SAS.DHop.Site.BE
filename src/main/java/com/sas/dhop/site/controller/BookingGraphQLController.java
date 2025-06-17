package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.response.BookingGraphQLResponse;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "[Booking GraphQL Controller]")
public class BookingGraphQLController {

    private final BookingRepository bookingRepository;

    /**
     * Get all bookings with full GraphQL fields including createdAt/updatedAt
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookings() {
        log.info("Fetching all bookings via GraphQL");
        return bookingRepository.findAll().stream()
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get booking by ID with full GraphQL fields
     */
    @QueryMapping
    public BookingGraphQLResponse booking(@Argument String id) {
        log.info("Fetching booking with id: {}", id);
        Booking booking = bookingRepository.findById(Integer.valueOf(id))
                .orElse(null);
        return booking != null ? BookingGraphQLResponse.mapToBookingGraphQLResponse(booking) : null;
    }

    /**
     * Get all bookings for a specific user (customer)
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByUser(@Argument String userId) {
        log.info("Fetching bookings for user with id: {}", userId);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getCustomer().getId().equals(Integer.valueOf(userId)))
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get all bookings for a specific dancer
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByDancer(@Argument String dancerId) {
        log.info("Fetching bookings for dancer with id: {}", dancerId);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getDancer() != null && 
                        booking.getDancer().getId().equals(Integer.valueOf(dancerId)))
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get all bookings for a specific choreographer
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByChoreographer(@Argument String choreographerId) {
        log.info("Fetching bookings for choreographer with id: {}", choreographerId);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getChoreography() != null && 
                        booking.getChoreography().getId().equals(Integer.valueOf(choreographerId)))
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings by status
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByStatus(@Argument String status) {
        log.info("Fetching bookings with status: {}", status);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus().getStatusName().equalsIgnoreCase(status))
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings within a custom date range
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByDateRange(
            @Argument String startDate, 
            @Argument String endDate) {
        log.info("Fetching bookings from {} to {}", startDate, endDate);
        
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            return bookingRepository.findAll().stream()
                    .filter(booking -> {
                        if (booking.getCreatedAt() != null) {
                            LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                            return !bookingDate.isBefore(start) && !bookingDate.isAfter(end);
                        }
                        return false;
                    })
                    .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Error parsing dates: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Get bookings for today
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsToday() {
        log.info("Fetching bookings for today");
        LocalDate today = LocalDate.now();
        
        return bookingRepository.findAll().stream()
                .filter(booking -> {
                    if (booking.getCreatedAt() != null) {
                        LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                        return bookingDate.equals(today);
                    }
                    return false;
                })
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings for this week
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsThisWeek() {
        log.info("Fetching bookings for this week");
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        
        return bookingRepository.findAll().stream()
                .filter(booking -> {
                    if (booking.getCreatedAt() != null) {
                        LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                        return !bookingDate.isBefore(startOfWeek) && !bookingDate.isAfter(endOfWeek);
                    }
                    return false;
                })
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings for this month
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsThisMonth() {
        log.info("Fetching bookings for this month");
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        return bookingRepository.findAll().stream()
                .filter(booking -> {
                    if (booking.getCreatedAt() != null) {
                        LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                        return !bookingDate.isBefore(startOfMonth) && !bookingDate.isAfter(endOfMonth);
                    }
                    return false;
                })
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings for this year
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsThisYear() {
        log.info("Fetching bookings for this year");
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate endOfYear = today.withDayOfYear(today.lengthOfYear());
        
        return bookingRepository.findAll().stream()
                .filter(booking -> {
                    if (booking.getCreatedAt() != null) {
                        LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                        return !bookingDate.isBefore(startOfYear) && !bookingDate.isAfter(endOfYear);
                    }
                    return false;
                })
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }

    /**
     * Get bookings for a specific period (flexible)
     */
    @QueryMapping
    public List<BookingGraphQLResponse> bookingsByPeriod(
            @Argument String period,
            @Argument Integer offset) {
        log.info("Fetching bookings for period: {} with offset: {}", period, offset);
        
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;
        
        switch (period.toUpperCase()) {
            case "DAY":
                startDate = today.plusDays(offset != null ? offset : 0);
                endDate = startDate;
                break;
            case "WEEK":
                LocalDate weekStart = today.plusWeeks(offset != null ? offset : 0).with(DayOfWeek.MONDAY);
                startDate = weekStart;
                endDate = weekStart.with(DayOfWeek.SUNDAY);
                break;
            case "MONTH":
                LocalDate monthStart = today.plusMonths(offset != null ? offset : 0).withDayOfMonth(1);
                startDate = monthStart;
                endDate = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
                break;
            case "YEAR":
                LocalDate yearStart = today.plusYears(offset != null ? offset : 0).withDayOfYear(1);
                startDate = yearStart;
                endDate = yearStart.withDayOfYear(yearStart.lengthOfYear());
                break;
            default:
                log.warn("Unknown period: {}, defaulting to today", period);
                startDate = today;
                endDate = today;
        }
        
        return bookingRepository.findAll().stream()
                .filter(booking -> {
                    if (booking.getCreatedAt() != null) {
                        LocalDate bookingDate = booking.getCreatedAt().toLocalDate();
                        return !bookingDate.isBefore(startDate) && !bookingDate.isAfter(endDate);
                    }
                    return false;
                })
                .map(BookingGraphQLResponse::mapToBookingGraphQLResponse)
                .toList();
    }
} 