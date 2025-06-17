package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.BookingStatus;
import com.sas.dhop.site.constant.UserSubscriptionStatus;
import com.sas.dhop.site.dto.response.AdminDashboardResponse;
import com.sas.dhop.site.dto.response.BookingStatisticsResponse;
import com.sas.dhop.site.dto.response.RevenueStatisticsResponse;
import com.sas.dhop.site.model.Booking;
import com.sas.dhop.site.model.UserSubscription;
import com.sas.dhop.site.repository.BookingRepository;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.repository.DancerRepository;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.UserSubscriptionRepository;
import com.sas.dhop.site.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Admin Dashboard Service]")
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final EntityManager entityManager;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        log.info("Generating comprehensive admin dashboard data");
        
        // Get total counts
        Long totalBookings = bookingRepository.count();
        Long totalUsers = userRepository.count();
        Long totalDancers = dancerRepository.count();
        Long totalChoreographers = choreographyRepository.count();
        
        // Get revenue data
        BigDecimal totalRevenue = calculateTotalRevenue();
        BigDecimal bookingRevenue = calculateBookingRevenue();
        BigDecimal subscriptionRevenue = calculateSubscriptionRevenue();
        
        // Get time-based statistics
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        List<BookingStatisticsResponse> dailyBookings = getBookingStatisticsByPeriod("DAILY", 7);
        List<BookingStatisticsResponse> weeklyBookings = getBookingStatisticsByPeriod("WEEKLY", 4);
        List<BookingStatisticsResponse> monthlyBookings = getBookingStatisticsByPeriod("MONTHLY", 12);
        
        // Get booking status breakdown
        Long activeBookings = countBookingsByStatus(BookingStatus.BOOKING_ACTIVATE);
        Long completedBookings = countBookingsByStatus(BookingStatus.BOOKING_COMPLETED);
        Long canceledBookings = countBookingsByStatus(BookingStatus.BOOKING_CANCELED);
        Long pendingBookings = countBookingsByStatus(BookingStatus.BOOKING_PENDING);
        
        return AdminDashboardResponse.builder()
                .totalBookings(totalBookings)
                .totalUsers(totalUsers)
                .totalDancers(totalDancers)
                .totalChoreographers(totalChoreographers)
                .totalRevenue(totalRevenue)
                .bookingRevenue(bookingRevenue)
                .subscriptionRevenue(subscriptionRevenue)
                .thisMonthRevenue(calculateThisMonthRevenue())
                .lastMonthRevenue(calculateLastMonthRevenue())
                .revenueGrowthRate(calculateRevenueGrowthRate())
                .activeBookings(activeBookings)
                .completedBookings(completedBookings)
                .canceledBookings(canceledBookings)
                .pendingBookings(pendingBookings)
                .dailyBookings(dailyBookings)
                .weeklyBookings(weeklyBookings)
                .monthlyBookings(monthlyBookings)
                .revenueByService(getRevenueByService())
                .monthlyRevenueBreakdown(getMonthlyRevenueBreakdown())
                .averageBookingValue(calculateAverageBookingValue())
                .activeSubscriptions(countActiveSubscriptions())
                .expiredSubscriptions(countExpiredSubscriptions())
                .dataFromDate(startDate)
                .dataToDate(endDate)
                .build();
    }

    @Override
    public BookingStatisticsResponse getBookingStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        Long count = countBookingsByDateRange(startDateTime, endDateTime);
        Long previousCount = countBookingsByDateRange(
                startDateTime.minusDays(endDate.toEpochDay() - startDate.toEpochDay() + 1),
                startDateTime.minusDays(1)
        );
        
        return BookingStatisticsResponse.builder()
                .label("Booking Statistics")
                .count(count)
                .dateFrom(startDate)
                .dateTo(endDate)
                .period("CUSTOM")
                .percentage(100.0)
                .previousCount(previousCount)
                .growthRate(calculateGrowthRate(count, previousCount))
                .build();
    }

    @Override
    public List<BookingStatisticsResponse> getBookingStatisticsByPeriod(String period, int count) {
        List<BookingStatisticsResponse> statistics = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            LocalDate endDate = LocalDate.now().minusDays(i * getDaysForPeriod(period));
            LocalDate startDate = endDate.minusDays(getDaysForPeriod(period) - 1);
            
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            
            Long bookingCount = countBookingsByDateRange(startDateTime, endDateTime);
            
            LocalDateTime prevStartDateTime = startDateTime.minusDays(getDaysForPeriod(period));
            LocalDateTime prevEndDateTime = endDateTime.minusDays(getDaysForPeriod(period));
            Long previousCount = countBookingsByDateRange(prevStartDateTime, prevEndDateTime);
            
            statistics.add(BookingStatisticsResponse.builder()
                    .label(formatPeriodLabel(endDate, period))
                    .count(bookingCount)
                    .dateFrom(startDate)
                    .dateTo(endDate)
                    .period(period)
                    .percentage(null)
                    .previousCount(previousCount)
                    .growthRate(calculateGrowthRate(bookingCount, previousCount))
                    .build());
        }
        
        return statistics;
    }

    @Override
    public List<BookingStatisticsResponse> getBookingCountsByStatus(List<String> statuses) {
        List<BookingStatisticsResponse> result = new ArrayList<>();
        Long totalBookings = bookingRepository.count();
        
        log.info("Getting booking counts for statuses: {}, Total bookings in DB: {}", statuses, totalBookings);
        
        // Debug: Log all existing booking statuses in the database
        try {
            String debugJpql = "SELECT DISTINCT b.bookingStatus FROM Booking b WHERE b.bookingStatus IS NOT NULL";
            Query debugQuery = entityManager.createQuery(debugJpql);
            List<String> existingStatuses = debugQuery.getResultList();
            log.info("Existing booking statuses in database: {}", existingStatuses);
        } catch (Exception e) {
            log.warn("Could not fetch existing booking statuses for debug: {}", e.getMessage());
        }
        
        for (String status : statuses) {
            Long count = countBookingsByStatus(status);
            log.info("Status '{}' count: {}", status, count);
            
            result.add(BookingStatisticsResponse.builder()
                    .label(status)
                    .count(count)
                    .period("STATUS")
                    .status(status)
                    .percentage(totalBookings > 0 ? (count.doubleValue() / totalBookings) * 100 : 0.0)
                    .build());
        }
        
        log.info("Final booking status results: {}", result.size());
        return result;
    }

    @Override
    public RevenueStatisticsResponse getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        BigDecimal revenue = calculateRevenueForPeriod(startDate, endDate);
        Long transactionCount = countBookingsByDateRange(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        return RevenueStatisticsResponse.builder()
                .label("Total Revenue")
                .amount(revenue)
                .currency("VND")
                .dateFrom(startDate)
                .dateTo(endDate)
                .revenueType("TOTAL")
                .transactionCount(transactionCount)
                .averageTransactionValue(transactionCount > 0 ? 
                    revenue.divide(BigDecimal.valueOf(transactionCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO)
                .build();
    }

    @Override
    public Double getTotalWebsiteRevenue() {
        return calculateTotalRevenue().doubleValue();
    }

    @Override
    public List<RevenueStatisticsResponse> getSubscriptionRevenue(LocalDate startDate, LocalDate endDate) {
        List<RevenueStatisticsResponse> revenues = new ArrayList<>();
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        // Get subscription revenue and transaction count
        Object[] subscriptionData = getSubscriptionRevenueData(startDateTime, endDateTime);
        BigDecimal subscriptionRevenue = (BigDecimal) subscriptionData[0];
        Long subscriptionCount = (Long) subscriptionData[1];
        
        revenues.add(RevenueStatisticsResponse.builder()
                .label("Premium Subscription")
                .amount(subscriptionRevenue)
                .currency("VND")
                .revenueType("SUBSCRIPTION")
                .transactionCount(subscriptionCount)
                .averageTransactionValue(subscriptionCount > 0 ? 
                    subscriptionRevenue.divide(BigDecimal.valueOf(subscriptionCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO)
                .build());
        
        return revenues;
    }

    @Override
    public List<RevenueStatisticsResponse> getBookingRevenue(LocalDate startDate, LocalDate endDate) {
        List<RevenueStatisticsResponse> revenues = new ArrayList<>();
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        // Get booking revenue
        BigDecimal bookingRevenue = calculateBookingRevenueForPeriod(startDateTime, endDateTime);
        Long bookingCount = countBookingsByDateRange(startDateTime, endDateTime);
        
        revenues.add(RevenueStatisticsResponse.builder()
                .label("Dancer Bookings")
                .amount(bookingRevenue)
                .currency("VND")
                .revenueType("BOOKING")
                .transactionCount(bookingCount)
                .averageTransactionValue(bookingCount > 0 ? 
                    bookingRevenue.divide(BigDecimal.valueOf(bookingCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO)
                .build());
        
        return revenues;
    }

    // Helper methods with real database queries
    private BigDecimal calculateTotalRevenue() {
        BigDecimal bookingRevenue = calculateBookingRevenue();
        BigDecimal subscriptionRevenue = calculateSubscriptionRevenue();
        return bookingRevenue.add(subscriptionRevenue);
    }

    private BigDecimal calculateBookingRevenue() {
        String jpql = "SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE b.price IS NOT NULL";
        Query query = entityManager.createQuery(jpql);
        Object result = query.getSingleResult();
        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Double) {
            return BigDecimal.valueOf((Double) result);
        } else if (result instanceof Long) {
            return BigDecimal.valueOf((Long) result);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateSubscriptionRevenue() {
        String jpql = "SELECT COALESCE(SUM(s.price), 0) FROM UserSubscription us " +
                     "JOIN us.subscription s WHERE us.status.statusName = :activeStatus";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("activeStatus", UserSubscriptionStatus.ACTIVE_USER_SUBSCRIPTION);
        Object result = query.getSingleResult();
        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Double) {
            return BigDecimal.valueOf((Double) result);
        } else if (result instanceof Long) {
            return BigDecimal.valueOf((Long) result);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateThisMonthRevenue() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);
        
        return calculateRevenueForPeriod(startOfMonth.toLocalDate(), endOfMonth.toLocalDate());
    }

    private BigDecimal calculateLastMonthRevenue() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);
        
        return calculateRevenueForPeriod(startOfMonth.toLocalDate(), endOfMonth.toLocalDate());
    }

    private Double calculateRevenueGrowthRate() {
        BigDecimal thisMonth = calculateThisMonthRevenue();
        BigDecimal lastMonth = calculateLastMonthRevenue();
        
        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
            return thisMonth.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        
        return thisMonth.subtract(lastMonth)
                .divide(lastMonth, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private Double calculateAverageBookingValue() {
        String jpql = "SELECT AVG(b.price) FROM Booking b WHERE b.price IS NOT NULL";
        Query query = entityManager.createQuery(jpql);
        Object result = query.getSingleResult();
        if (result == null) {
            return 0.0;
        }
        // AVG() returns Double, not BigDecimal
        if (result instanceof Double) {
            return (Double) result;
        } else if (result instanceof BigDecimal) {
            return ((BigDecimal) result).doubleValue();
        }
        return 0.0;
    }

    private Long countBookingsByStatus(String status) {
        // The frontend passes status constant keys like "BOOKING_PENDING", but the database contains English status values
        // Based on logs, database has: [COMPLETED, CONFIRMED, CANCELLED, PENDING, IN_PROGRESS, TRIAL, RESCHEDULED]
        // We need to map frontend constants to actual database values
        String databaseStatus;
        switch (status) {
            case "BOOKING_PENDING":
                databaseStatus = "PENDING";
                break;
            case "BOOKING_ACTIVATE":
                databaseStatus = "CONFIRMED"; // Assuming CONFIRMED means activated/approved
                break;
            case "BOOKING_CANCELED":
                databaseStatus = "CANCELLED";
                break;
            case "BOOKING_COMPLETED":
                databaseStatus = "COMPLETED";
                break;
            case "BOOKING_IN_PROGRESS":
                databaseStatus = "IN_PROGRESS";
                break;
            case "BOOKING_TRIAL":
                databaseStatus = "TRIAL";
                break;
            case "BOOKING_RESCHEDULED":
                databaseStatus = "RESCHEDULED";
                break;
            default:
                databaseStatus = status; // fallback to original status
        }
        
        log.debug("Mapping frontend status '{}' to database status '{}'", status, databaseStatus);
        
        String jpql = "SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("status", databaseStatus);
        Long result = (Long) query.getSingleResult();
        
        log.debug("Query result for status '{}' ({}): {}", status, databaseStatus, result);
        return result;
    }

    private Long countBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT COUNT(b) FROM Booking b WHERE b.createdAt BETWEEN :startDate AND :endDate";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return (Long) query.getSingleResult();
    }

    private Long countActiveSubscriptions() {
        String jpql = "SELECT COUNT(us) FROM UserSubscription us WHERE us.status.statusName = :activeStatus";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("activeStatus", UserSubscriptionStatus.ACTIVE_USER_SUBSCRIPTION);
        return (Long) query.getSingleResult();
    }

    private Long countExpiredSubscriptions() {
        String jpql = "SELECT COUNT(us) FROM UserSubscription us WHERE us.status.statusName = :expiredStatus";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("expiredStatus", UserSubscriptionStatus.EXPIRE_USER_SUBSCRIPTION);
        return (Long) query.getSingleResult();
    }

    private BigDecimal calculateRevenueForPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        BigDecimal bookingRevenue = calculateBookingRevenueForPeriod(startDateTime, endDateTime);
        BigDecimal subscriptionRevenue = calculateSubscriptionRevenueForPeriod(startDateTime, endDateTime);
        
        return bookingRevenue.add(subscriptionRevenue);
    }

    private BigDecimal calculateBookingRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT COALESCE(SUM(b.price), 0) FROM Booking b " +
                     "WHERE b.createdAt BETWEEN :startDate AND :endDate AND b.price IS NOT NULL";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        Object result = query.getSingleResult();
        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Double) {
            return BigDecimal.valueOf((Double) result);
        } else if (result instanceof Long) {
            return BigDecimal.valueOf((Long) result);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateSubscriptionRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT COALESCE(SUM(s.price), 0) FROM UserSubscription us " +
                     "JOIN us.subscription s " +
                     "WHERE us.createdAt BETWEEN :startDate AND :endDate " +
                     "AND us.status.statusName = :activeStatus";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("activeStatus", UserSubscriptionStatus.ACTIVE_USER_SUBSCRIPTION);
        Object result = query.getSingleResult();
        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Double) {
            return BigDecimal.valueOf((Double) result);
        } else if (result instanceof Long) {
            return BigDecimal.valueOf((Long) result);
        }
        return BigDecimal.ZERO;
    }

    private Object[] getSubscriptionRevenueData(LocalDateTime startDate, LocalDateTime endDate) {
        String jpql = "SELECT COALESCE(SUM(s.price), 0), COUNT(us) FROM UserSubscription us " +
                     "JOIN us.subscription s " +
                     "WHERE us.createdAt BETWEEN :startDate AND :endDate " +
                     "AND us.status.statusName = :activeStatus";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("activeStatus", UserSubscriptionStatus.ACTIVE_USER_SUBSCRIPTION);
        Object[] result = (Object[]) query.getSingleResult();
        
        // Handle potential type casting issues for the sum result
        Object sumResult = result[0];
        BigDecimal revenue;
        if (sumResult instanceof BigDecimal) {
            revenue = (BigDecimal) sumResult;
        } else if (sumResult instanceof Double) {
            revenue = BigDecimal.valueOf((Double) sumResult);
        } else if (sumResult instanceof Long) {
            revenue = BigDecimal.valueOf((Long) sumResult);
        } else {
            revenue = BigDecimal.ZERO;
        }
        
        return new Object[]{revenue, result[1]};
    }

    private List<RevenueStatisticsResponse> getRevenueByService() {
        List<RevenueStatisticsResponse> revenues = new ArrayList<>();
        
        BigDecimal bookingRevenue = calculateBookingRevenue();
        BigDecimal subscriptionRevenue = calculateSubscriptionRevenue();
        BigDecimal totalRevenue = bookingRevenue.add(subscriptionRevenue);
        
        log.info("Revenue by Service calculation - Booking: {}, Subscription: {}, Total: {}", 
                bookingRevenue, subscriptionRevenue, totalRevenue);
        
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            revenues.add(RevenueStatisticsResponse.builder()
                    .label("Booking Commission")
                    .amount(bookingRevenue)
                    .revenueType("BOOKING")
                    .percentage(bookingRevenue.divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP)
                              .multiply(BigDecimal.valueOf(100)).doubleValue())
                    .build());
            
            revenues.add(RevenueStatisticsResponse.builder()
                    .label("Subscription Fees")
                    .amount(subscriptionRevenue)
                    .revenueType("SUBSCRIPTION")
                    .percentage(subscriptionRevenue.divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP)
                              .multiply(BigDecimal.valueOf(100)).doubleValue())
                    .build());
            
            log.info("Generated {} revenue service entries", revenues.size());
        } else {
            log.warn("Total revenue is zero, no revenue by service data generated");
        }
        
        return revenues;
    }

    private List<RevenueStatisticsResponse> getMonthlyRevenueBreakdown() {
        List<RevenueStatisticsResponse> breakdown = new ArrayList<>();
        
        for (int i = 0; i < 12; i++) {
            YearMonth month = YearMonth.now().minusMonths(i);
            LocalDate startDate = month.atDay(1);
            LocalDate endDate = month.atEndOfMonth();
            
            BigDecimal amount = calculateRevenueForPeriod(startDate, endDate);
            
            breakdown.add(RevenueStatisticsResponse.builder()
                    .label(month.format(DateTimeFormatter.ofPattern("MMM yyyy")))
                    .amount(amount)
                    .dateFrom(startDate)
                    .dateTo(endDate)
                    .period("MONTHLY")
                    .revenueType("TOTAL")
                    .build());
        }
        
        return breakdown;
    }

    private Double calculateGrowthRate(Long current, Long previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current.doubleValue() - previous.doubleValue()) / previous.doubleValue()) * 100;
    }

    private int getDaysForPeriod(String period) {
        return switch (period) {
            case "DAILY" -> 1;
            case "WEEKLY" -> 7;
            case "MONTHLY" -> 30;
            case "YEARLY" -> 365;
            default -> 1;
        };
    }

    private String formatPeriodLabel(LocalDate date, String period) {
        return switch (period) {
            case "DAILY" -> date.format(DateTimeFormatter.ofPattern("dd/MM"));
            case "WEEKLY" -> "Week " + date.format(DateTimeFormatter.ofPattern("w"));
            case "MONTHLY" -> date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
            case "YEARLY" -> date.format(DateTimeFormatter.ofPattern("yyyy"));
            default -> date.toString();
        };
    }
} 