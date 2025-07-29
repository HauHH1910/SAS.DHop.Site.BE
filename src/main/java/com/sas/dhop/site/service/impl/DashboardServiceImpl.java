package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.BookingStatus.*;

import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.service.DashboardService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.util.mapper.UserMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final AuthenticationService authenticationService;
    private final StatusService statusService;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserMapper userMapper;
    private final BookingFeedbackRepository bookingFeedbackRepository;

    @Override
    public List<BookingDetailResponse> getBookingDetails(String bookingStatus) {

        if (bookingStatus != null) {

            Status status = statusService.findStatusOrCreated(bookingStatus);

            return bookingRepository.findAllByStatus(status).stream()
                    .map(BookingDetailResponse::mapToBookingDetail)
                    .toList();
        }

        return bookingRepository.findAll().stream()
                .map(BookingDetailResponse::mapToBookingDetail)
                .toList();
    }

    @Override
    public OverviewStatisticsResponse overviewStatistics() {
        boolean isAdmin = authenticationService.authenticationChecking(RolePrefix.ADMIN_PREFIX);
        if (!isAdmin) {
            throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
        }

        long totalUser = userRepository.count();
        long totalBookings = bookingRepository.count();

        Set<String> validBookingStatuses = Set.of(
                BOOKING_PENDING,
                BOOKING_ACTIVATE,
                BOOKING_COMPLETED,
                BOOKING_SENT_IMAGE,
                BOOKING_IN_PROGRESS,
                BOOKING_WORKING_DONE,
                BOOKING_ACCEPTED,
                BOOKING_HAD_FEED_BACK);

        List<Booking> validBookings = bookingRepository.findAll().stream()
                .filter(booking ->
                        validBookingStatuses.contains(booking.getStatus().getStatusName()))
                .toList();
        BigDecimal totalBookingPrice = BigDecimal.ZERO; // Tổng giá gốc
        BigDecimal totalCommissionRevenue = BigDecimal.ZERO; // Tổng hoa hồng
        for (Booking booking : validBookings) {
            BigDecimal price = booking.getPrice(); // 500,000 VND
            totalBookingPrice = totalBookingPrice.add(price);
            totalCommissionRevenue = totalCommissionRevenue.add(calculateCommissionPrice(price)); // 100,000 VND
        }

        long totalRating = bookingFeedbackRepository.findAll().size();

        BigDecimal totalSubscriptionPayments = BigDecimal.ZERO;
        List<Subscription> subscriptions = userSubscriptionRepository.findAll().stream()
                .map(UserSubscription::getSubscription)
                .toList();

        for(Subscription subscription : subscriptions) {
            totalSubscriptionPayments = totalSubscriptionPayments.add(subscription.getPrice());
        }

        return OverviewStatisticsResponse.builder()
                .totalUsers(totalUser)
                .totalBookings(totalBookings)
                .totalRating(totalRating)
                .totalSubscriptionRevenue(totalSubscriptionPayments.longValueExact())
                .totalBookingRevenue(totalCommissionRevenue.longValueExact())
                .build();
    }

    private BigDecimal calculateCommissionPrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("500000")) >= 0 && price.compareTo(new BigDecimal("1000000")) < 0) {
            // Phí hoa hồng 20%
            return price.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP);
        } else if (price.compareTo(new BigDecimal("1000000")) >= 0 && price.compareTo(new BigDecimal("2000000")) < 0) {
            // Phí hoa hồng 15%
            return price.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Phí hoa hồng 10% (>= 2 triệu)
            return price.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public List<UserResponse> userManagement() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
                        .contains(RoleName.ADMIN))
                .map(userMapper::mapToUserResponse)
                .toList();
    }

    @Override
    public List<BookingStatisticsResponse> getBookingStatistics(
            String timeFrame, LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        // Default to current date if not provided (except for custom timeframe)
        if (date == null && !"custom".equals(timeFrame)) {
            date = LocalDateTime.now();
        }

        // For custom timeframe, validate dates
        if ("custom".equals(timeFrame)) {
            if (startDate == null || endDate == null) {
                // If missing dates for custom range, default to current month
                return getMonthlyBookingStats(LocalDateTime.now());
            }

            if (startDate.isAfter(endDate)) {
                throw new BusinessException(ErrorConstant.INVALID_DATE_RANGE);
            }

            return getCustomRangeBookingStats(startDate, endDate);
        }

        // Handle standard timeframes
        return switch (timeFrame) {
            case "week" -> getWeeklyBookingStats(date);
            case "month" -> getMonthlyBookingStats(date);
            case "year" -> getYearlyBookingStats(date);
            default -> getMonthlyBookingStats(date);
        };
    }



    private List<BookingStatisticsResponse> getWeeklyBookingStats(LocalDateTime date) {
        // Determine the start and end of the week based on the given date
        LocalDateTime startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);

        // Get all bookings for the week
        List<Object[]> bookingsCountByDay = bookingRepository.countBookingsByDateRange(startOfWeek, endOfWeek);
        List<Object[]> revenueByDay = bookingRepository.sumRevenueByDateRange(startOfWeek, endOfWeek);

        // Convert results to maps for easier access
        Map<LocalDate, Long> bookingsMap = new HashMap<>();
        Map<LocalDate, Double> revenueMap = new HashMap<>();

        for (Object[] result : bookingsCountByDay) {
            if (result[0] instanceof java.sql.Date) {
                LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                Long count = result[1] == null ? 0L : ((Number) result[1]).longValue();
                bookingsMap.put(day, count);
            }
        }

        for (Object[] result : revenueByDay) {
            if (result[0] instanceof java.sql.Date) {
                LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                Double amount = result[1] == null ? 0.0 : ((Number) result[1]).doubleValue();
                revenueMap.put(day, amount);
            }
        }

        // Format for day display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd/MM");

        // Create a response for each day of the week
        return IntStream.range(0, 7)
                .mapToObj(startOfWeek::plusDays)
                .map(day -> {
                    String label = day.format(formatter);
                    LocalDate dayDate = day.toLocalDate();
                    int bookings = bookingsMap.getOrDefault(dayDate, 0L).intValue();
                    double revenue = revenueMap.getOrDefault(dayDate, 0.0);

                    return BookingStatisticsResponse.builder()
                            .label(label)
                            .bookings(bookings)
                            .revenue(revenue)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<BookingStatisticsResponse> getMonthlyBookingStats(LocalDateTime date) {
        // Get the year and month from the given date
        YearMonth yearMonth = YearMonth.from(date);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atStartOfDay();

        // Get all bookings for the month
        List<Object[]> bookingsCountByDay = bookingRepository.countBookingsByDateRange(startOfMonth, endOfMonth);
        List<Object[]> revenueByDay = bookingRepository.sumRevenueByDateRange(startOfMonth, endOfMonth);

        // Convert results to maps for easier access
        Map<LocalDate, Long> bookingsMap = new HashMap<>();
        Map<LocalDate, Double> revenueMap = new HashMap<>();

        for (Object[] result : bookingsCountByDay) {
            if (result[0] instanceof java.sql.Date) {
                LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                Long count = result[1] == null ? 0L : ((Number) result[1]).longValue();
                bookingsMap.put(day, count);
            }
        }

        for (Object[] result : revenueByDay) {
            if (result[0] instanceof java.sql.Date) {
                LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                Double amount = result[1] == null ? 0.0 : ((Number) result[1]).doubleValue();
                revenueMap.put(day, amount);
            }
        }

        // Create a response for each week of the month
        List<BookingStatisticsResponse> result = new ArrayList<>();

        // Group by week
        LocalDateTime current = startOfMonth;
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentWeek = current.get(weekFields.weekOfWeekBasedYear());

        while (!current.isAfter(endOfMonth)) {
            LocalDateTime weekStart = current;
            int week = current.get(weekFields.weekOfWeekBasedYear());

            // Move to next week or end of month
            while (!current.isAfter(endOfMonth) && current.get(weekFields.weekOfWeekBasedYear()) == week) {
                current = current.plusDays(1);
            }

            LocalDateTime weekEnd = current.minusDays(1);

            // Calculate totals for this week
            long bookingsCount = 0;
            double revenue = 0;
            for (LocalDateTime day = weekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
                LocalDate dayDate = day.toLocalDate();
                bookingsCount += bookingsMap.getOrDefault(dayDate, 0L);
                revenue += revenueMap.getOrDefault(dayDate, 0.0);
            }

            // Add week statistics
            String label = String.format(
                    "Week %d (%s - %s)",
                    week,
                    weekStart.format(DateTimeFormatter.ofPattern("dd/MM")),
                    weekEnd.format(DateTimeFormatter.ofPattern("dd/MM")));

            result.add(BookingStatisticsResponse.builder()
                    .label(label)
                    .bookings((int) bookingsCount)
                    .revenue(revenue)
                    .build());
        }

        return result;
    }

    private List<BookingStatisticsResponse> getYearlyBookingStats(LocalDateTime date) {
        int year = date.getYear();

        // Get monthly totals
        List<Object[]> bookingsCountByMonth = bookingRepository.countBookingsByMonthInYear(year);
        List<Object[]> revenueByMonth = bookingRepository.sumRevenueByMonthInYear(year);

        // Convert results to maps for easier access
        Map<String, Long> bookingsMap = new HashMap<>();
        Map<String, Double> revenueMap = new HashMap<>();

        for (Object[] result : bookingsCountByMonth) {
            if (result[0] instanceof String) {
                String yearMonth = (String) result[0];
                Long count = result[1] == null ? 0L : ((Number) result[1]).longValue();
                bookingsMap.put(yearMonth, count);
            }
        }

        for (Object[] result : revenueByMonth) {
            if (result[0] instanceof String) {
                String yearMonth = (String) result[0];
                Double amount = result[1] == null ? 0.0 : ((Number) result[1]).doubleValue();
                revenueMap.put(yearMonth, amount);
            }
        }

        // Create a response for each month
        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    YearMonth yearMonthObj = YearMonth.of(year, month);
                    String yearMonthStr = String.format("%d-%02d", year, month);
                    String label = yearMonthObj.format(DateTimeFormatter.ofPattern("MMMM"));
                    int bookings = bookingsMap.getOrDefault(yearMonthStr, 0L).intValue();
                    double revenue = revenueMap.getOrDefault(yearMonthStr, 0.0);

                    return BookingStatisticsResponse.builder()
                            .label(label)
                            .bookings(bookings)
                            .revenue(revenue)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<BookingStatisticsResponse> getCustomRangeBookingStats(LocalDateTime startDate, LocalDateTime endDate) {
        // If the range is too large, we group by weeks or months
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        if (daysBetween <= 31) {
            // For a month or less, show daily stats
            List<Object[]> bookingsCountByDay = bookingRepository.countBookingsByDateRange(startDate, endDate);
            List<Object[]> revenueByDay = bookingRepository.sumRevenueByDateRange(startDate, endDate);

            // Convert results to maps for easier access
            Map<LocalDate, Long> bookingsMap = new HashMap<>();
            Map<LocalDate, Double> revenueMap = new HashMap<>();

            for (Object[] result : bookingsCountByDay) {
                if (result[0] instanceof java.sql.Date) {
                    LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                    Long count = result[1] == null ? 0L : ((Number) result[1]).longValue();
                    bookingsMap.put(day, count);
                }
            }

            for (Object[] result : revenueByDay) {
                if (result[0] instanceof java.sql.Date) {
                    LocalDate day = ((java.sql.Date) result[0]).toLocalDate();
                    Double amount = result[1] == null ? 0.0 : ((Number) result[1]).doubleValue();
                    revenueMap.put(day, amount);
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            return IntStream.rangeClosed(0, (int) daysBetween)
                    .mapToObj(startDate::plusDays)
                    .map(day -> {
                        String label = day.format(formatter);
                        LocalDate dayDate = day.toLocalDate();
                        int bookings = bookingsMap.getOrDefault(dayDate, 0L).intValue();
                        double revenue = revenueMap.getOrDefault(dayDate, 0.0);

                        return BookingStatisticsResponse.builder()
                                .label(label)
                                .bookings(bookings)
                                .revenue(revenue)
                                .build();
                    })
                    .collect(Collectors.toList());
        } else if (daysBetween <= 90) {
            // For up to 3 months, group by week
            List<BookingStatisticsResponse> result = new ArrayList<>();
            List<Object[]> bookingsCountByDay = bookingRepository.countBookingsByDateRange(startDate, endDate);
            List<Object[]> revenueByDay = bookingRepository.sumRevenueByDateRange(startDate, endDate);

            // Convert results to maps for easier access
            Map<LocalDate, Long> bookingsMap = new HashMap<>();
            Map<LocalDate, Double> revenueMap = new HashMap<>();

            for (Object[] resultBooking : bookingsCountByDay) {
                if (resultBooking[0] instanceof java.sql.Date) {
                    LocalDate day = ((java.sql.Date) resultBooking[0]).toLocalDate();
                    Long count = resultBooking[1] == null ? 0L : ((Number) resultBooking[1]).longValue();
                    bookingsMap.put(day, count);
                }
            }

            for (Object[] resultRevenue : revenueByDay) {
                if (resultRevenue[0] instanceof java.sql.Date) {
                    LocalDate day = ((java.sql.Date) resultRevenue[0]).toLocalDate();
                    Double amount = resultRevenue[1] == null ? 0.0 : ((Number) resultRevenue[1]).doubleValue();
                    revenueMap.put(day, amount);
                }
            }

            LocalDateTime current = startDate;
            WeekFields weekFields = WeekFields.of(Locale.getDefault());

            while (!current.isAfter(endDate)) {
                // Start of current week (Monday)
                LocalDateTime weekStart = LocalDate.from(
                                current.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
                        .atStartOfDay();
                if (weekStart.isBefore(startDate)) {
                    weekStart = startDate;
                }

                // End of current week (Sunday)
                LocalDateTime weekEnd = weekStart.plusDays(6);
                if (weekEnd.isAfter(endDate)) {
                    weekEnd = endDate;
                }

                // Calculate totals for this week
                long bookingsCount = 0;
                double revenue = 0;
                for (LocalDateTime day = weekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
                    LocalDate dayDate = day.toLocalDate();
                    bookingsCount += bookingsMap.getOrDefault(dayDate, 0L);
                    revenue += revenueMap.getOrDefault(dayDate, 0.0);
                }

                // Add week statistics
                String label = String.format(
                        "%s - %s",
                        weekStart.format(DateTimeFormatter.ofPattern("dd/MM")),
                        weekEnd.format(DateTimeFormatter.ofPattern("dd/MM")));

                result.add(BookingStatisticsResponse.builder()
                        .label(label)
                        .bookings((int) bookingsCount)
                        .revenue(revenue)
                        .build());

                // Move to start of next week
                current = weekEnd.plusDays(1);
            }

            return result;
        } else {
            // For longer periods, group by month
            List<BookingStatisticsResponse> result = new ArrayList<>();
            List<Object[]> bookingsCountByMonth =
                    bookingRepository.countBookingsByDateRangeGroupByMonth(startDate, endDate);
            List<Object[]> revenueByMonth = bookingRepository.sumRevenueByDateRangeGroupByMonth(startDate, endDate);

            // Convert results to maps for easier access
            Map<String, Long> bookingsMap = new HashMap<>();
            Map<String, Double> revenueMap = new HashMap<>();

            for (Object[] resultBooking : bookingsCountByMonth) {
                if (resultBooking[0] instanceof String) {
                    String yearMonth = (String) resultBooking[0];
                    Long count = resultBooking[1] == null ? 0L : ((Number) resultBooking[1]).longValue();
                    bookingsMap.put(yearMonth, count);
                }
            }

            for (Object[] resultRevenue : revenueByMonth) {
                if (resultRevenue[0] instanceof String) {
                    String yearMonth = (String) resultRevenue[0];
                    Double amount = resultRevenue[1] == null ? 0.0 : ((Number) resultRevenue[1]).doubleValue();
                    revenueMap.put(yearMonth, amount);
                }
            }

            YearMonth current = YearMonth.from(startDate);
            YearMonth end = YearMonth.from(endDate);

            while (!current.isAfter(end)) {
                String yearMonthStr = String.format("%d-%02d", current.getYear(), current.getMonthValue());
                String label = current.format(DateTimeFormatter.ofPattern("MMM yyyy"));
                int bookings = bookingsMap.getOrDefault(yearMonthStr, 0L).intValue();
                double revenue = revenueMap.getOrDefault(yearMonthStr, 0.0);

                result.add(BookingStatisticsResponse.builder()
                        .label(label)
                        .bookings(bookings)
                        .revenue(revenue)
                        .build());

                current = current.plusMonths(1);
            }

            return result;
        }
    }
}
