package com.sas.dhop.site.service.impl;
import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.dto.request.ChoreographerDashboardRequest;
import com.sas.dhop.site.dto.request.DancerDashboardRequest;
import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.service.BookingFeedbackService;
import com.sas.dhop.site.service.DashboardService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.util.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final BookingRepository bookingRepository;
    private final AuthenticationService authenticationService;
    private final StatusRepository statusRepository;
//    private final UserSubscriptionRepository userSubscriptionRepository;
//    private final BookingFeedbackService bookingFeedbackService;
    private final DashboardMapper dashboardMapper;

    @Override
    public List<AdminDashboardResponse> getAdminDashboard(String statusName) {


        boolean isAdmin = authenticationService.authenticationChecking(RolePrefix.ADMIN_PREFIX);

        if (!isAdmin) {
            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
        }

        // Tổng số entity
        Long totalUsers = userRepository.count();
        Long totalDancers = dancerRepository.count();
        Long totalChoreographers = choreographyRepository.count();
        Long totalBookings = bookingRepository.count();


        // Lọc booking theo status người dùng truyền lên
        List<Booking> bookingByStatus;
        Optional<Status> status = null;
        if (statusName != null && !statusName.isBlank()) {
            status = statusRepository.findByStatusName(statusName);
            if (status == null){
                throw new BusinessException(ErrorConstant.STATUS_NOT_FOUND);
            }
            bookingByStatus = bookingRepository.findAllByStatus(status);
        } else {
            bookingByStatus = bookingRepository.findAll();
        }


        // Tính tổng doanh thu theo status
        BigDecimal totalRevenue = bookingByStatus.stream()
                .map(Booking::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // (Optional) Nếu bạn muốn lấy danh sách đã sắp xếp theo ngày tạo:
        Sort sortAsc = Sort.by(Sort.Direction.ASC, "createdAt");
        Sort sortDesc = Sort.by(Sort.Direction.DESC, "createdAt");

        List<Booking> sortedBookingsAsc = bookingRepository.findAll(sortAsc);
        List<Booking> sortedBookingsDesc = bookingRepository.findAll(sortDesc);
        List<User> usersByDateAsc = userRepository.findAll(sortAsc);
        List<User> usersByDateDesc = userRepository.findAll(sortDesc);

        // Có thể xử lý thêm: Top 5 gần nhất, biểu đồ tăng trưởng,... tại đây nếu muốn


        // Trả kết quả
        return List.of(dashboardMapper.toAdminDashboardResponse(
                totalUsers,
                totalDancers,
                totalChoreographers,
                totalBookings,
                totalRevenue
        ));
    }


    @Override
    public DancerDashboardResponse getDancerDashboard(DancerDashboardRequest request) {
        // Long totalBookings = bookingRepository.countByDancerId(request.dancerId());
        // Long completedBookings =
        // bookingRepository.countByDancerIdAndStatusId(request.dancerId(), 3); //
        // Assuming 3 is completed status
        // Long pendingBookings =
        // bookingRepository.countByDancerIdAndStatusId(request.dancerId(), 1); //
        // Assuming 1 is pending status
        //
        // BigDecimal totalEarnings =
        // bookingRepository.getTotalAmountOfActivatedBookingsByDancerId(request.dancerId());
        //
        // List<Booking> upcomingBookings =
        // bookingRepository.findTop5ByDancerIdAndStatusIdOrderByStartAsc(request.dancerId(),
        // 1);
        // List<BookingFeedback> recentFeedbacks =
        // bookingFeedbackService.getFeedbackByDancerId(request.dancerId());
        //
        // UserSubscription currentSubscription =
        // userSubscriptionRepository.findByUserId(request.dancerId())
        // .orElseThrow(() -> new RuntimeException("No subscription found"));
        //
        // return dashboardMapper.toDancerDashboardResponse(
        // totalBookings,
        // completedBookings,
        // pendingBookings,
        // totalEarnings,
        // upcomingBookings,
        // recentFeedbacks,
        // currentSubscription
        // );
        return null;
    }

    @Override
    public ChoreographerDashboardResponse getChoreographerDashboard(ChoreographerDashboardRequest request) {
        // Long totalBookings =
        // bookingRepository.countByChoreographyId(request.choreographerId());
        // Long completedBookings =
        // bookingRepository.countByChoreographyIdAndStatusId(request.choreographerId(),
        // 3);
        // Long pendingBookings =
        // bookingRepository.countByChoreographyIdAndStatusId(request.choreographerId(),
        // 1);
        //
        // BigDecimal totalEarnings =
        // bookingRepository.getTotalAmountOfActivatedBookingsByChoreographyId(request.choreographerId());
        //
        // List<Booking> upcomingBookings =
        // bookingRepository.findTop5ByChoreographyIdAndStatusIdOrderByStartAsc(request.choreographerId(),
        // 1);
        // List<BookingFeedback> recentFeedbacks =
        // bookingFeedbackService.getFeedbackByChoreographer(request.choreographerId());
        //
        // UserSubscription currentSubscription =
        // userSubscriptionRepository.findByUserId(request.choreographerId())
        // .orElseThrow(() -> new RuntimeException("No subscription found"));
        //
        // List<Dancer> topDancers = dancerRepository.findTop5ByOrderByPriceDesc();
        //
        // return dashboardMapper.toChoreographerDashboardResponse(
        // totalBookings,
        // completedBookings,
        // pendingBookings,
        // totalEarnings,
        // upcomingBookings,
        // recentFeedbacks,
        // currentSubscription,
        // topDancers
        // );
        return null;
    }
}
