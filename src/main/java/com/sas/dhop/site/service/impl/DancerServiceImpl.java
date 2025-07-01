package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.DancerStatus.ACTIVATED_DANCER;

import com.sas.dhop.site.constant.DancerStatus;
import com.sas.dhop.site.dto.request.DancerRequest;
import com.sas.dhop.site.dto.request.DancersFiltersRequest;
import com.sas.dhop.site.dto.response.DancerResponse;
import com.sas.dhop.site.dto.response.DancersFiltersResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.DancerService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.DancerMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Dancer Service]")
public class DancerServiceImpl implements DancerService {

    private final DancerRepository dancerRepository;
    private final DancerMapper dancerMapper;
    private final UserService userService;
    private final StatusService statusService;
    private final DanceTypeService danceTypeService;

    @Override
    public DancerResponse updateDancer(Integer dancerId, DancerRequest dancerRequest) {
        Dancer dancer = dancerRepository
                .findById(dancerId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        // Validate user is updating their own profile
        User currentUser = userService.getLoginUser();
        if (!dancer.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
        }

        // Update basic dancer profile information
        dancer.setDancerNickName(dancerRequest.dancerNickName());
        dancer.setYearExperience(dancerRequest.yearExperience());
        dancer.setTeamSize(dancerRequest.teamSize());
        dancer.setPrice(dancerRequest.price());
        // Update dance types
        if (dancerRequest.danceTypeId() != null && !dancerRequest.danceTypeId().isEmpty()) {
            Set<DanceType> danceTypes = new HashSet<>();
            for (Integer danceTypeId : dancerRequest.danceTypeId()) {
                DanceType danceType = danceTypeService.findDanceType(danceTypeId);
                danceTypes.add(danceType);
            }
            dancer.setDanceTypes(danceTypes);
        }

        Dancer updatedDancer = dancerRepository.save(dancer);

        return dancerMapper.mapToDancerResponse(updatedDancer);
    }

    @Override
    public DancerResponse removeDancer(Integer dancerId) {
        Dancer dancer = dancerRepository
                .findById(dancerId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        // Set status to inactive
        Status inactiveStatus = statusService.getStatus(DancerStatus.INACTIVE_DANCER);
        dancer.setStatus(inactiveStatus);

        Dancer updatedDancer = dancerRepository.save(dancer);

        return dancerMapper.mapToDancerResponse(updatedDancer);
    }

    @Override
    public DancerResponse getDancerById(Integer dancerId) {
        Dancer dancer = dancerRepository
                .findById(dancerId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));
        return dancerMapper.mapToDancerResponse(dancer);
    }

    @Override
    public DancerResponse getDancerByDanceType(Integer danceTypeId) {
        DanceType danceType = danceTypeService.findDanceType(danceTypeId);
        if (danceType == null) throw new BusinessException(ErrorConstant.NOT_FOUND_DANCE_TYPE);

        List<Dancer> allDancers = dancerRepository.findAll();

        for (Dancer dancer : allDancers) {
            if (dancer.getDanceTypes() != null && dancer.getDanceTypes().contains(danceType)) {
                return dancerMapper.mapToDancerResponse(dancer);
            }
        }
        throw new BusinessException(ErrorConstant.USER_NOT_FOUND);
    }

    @Override
    public List<DancerResponse> getallDancer() {
        User currentUser = userService.getLoginUser();

        Set<RoleName> collect =
                currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        boolean isAdmin = collect.contains(RoleName.ADMIN);

        boolean isStaff = collect.contains(RoleName.STAFF);

        List<Dancer> dancers;
        if (isStaff || isAdmin) {
            dancers = dancerRepository.findAll();
        } else {
            dancers = dancerRepository.findByStatus(statusService.findStatusOrCreated(ACTIVATED_DANCER));
        }

        return dancers.stream().map(dancerMapper::mapToDancerResponse).collect(Collectors.toList());
    }

    @Override
    public List<DancersFiltersResponse> getAllDancersFilters(DancersFiltersRequest dancersFiltersRequest) {

        Status activeDancers = statusService.getStatus(ACTIVATED_DANCER);

        List<Dancer> dancers = dancerRepository.findByStatus(activeDancers);

        List<Dancer> filtered = dancers.stream()
                .filter(dancer -> {
                    // 1. Lọc theo khu vực (dựa trên dancers_work_area_list)
                    Integer areaId = dancersFiltersRequest.areaId();
                    if (areaId != null) {
                        if (dancer.getAreas() == null
                                || dancer.getAreas().isEmpty()
                                || dancer.getAreas().stream()
                                        .noneMatch(area -> area.getId().equals(areaId))) {
                            return false;
                        }
                    }

                    // 2. Lọc theo team size
                    Integer minTeamSize = dancersFiltersRequest.teamSize();
                    if (minTeamSize != null) {
                        if (dancer.getTeamSize() == null || dancer.getTeamSize() < minTeamSize) {
                            return false;
                        }
                    }

                    // 3. Lọc theo khoảng giá
                    BigDecimal minPrice = dancersFiltersRequest.minPrice();
                    if (minPrice != null) {
                        if (dancer.getPrice() == null || dancer.getPrice().compareTo(minPrice) < 0) {
                            return false;
                        }
                    }

                    BigDecimal maxPrice = dancersFiltersRequest.maxPrice();
                    if (maxPrice != null) {
                        if (dancer.getPrice() == null || dancer.getPrice().compareTo(maxPrice) > 0) {
                            return false;
                        }
                    }

                    // 4. Lọc theo thể loại nhảy (ít nhất 1 thể loại trùng)
                    List<Integer> requestedDanceTypeIds = dancersFiltersRequest.danceTypeId();
                    if (requestedDanceTypeIds != null && !requestedDanceTypeIds.isEmpty()) {
                        if (dancer.getDanceTypes() == null
                                || dancer.getDanceTypes().isEmpty()) {
                            return false;
                        }

                        boolean hasMatchingDanceType = dancer.getDanceTypes().stream()
                                .anyMatch(dt -> requestedDanceTypeIds.contains(dt.getId()));

                        if (!hasMatchingDanceType) {
                            return false;
                        }
                    }

                    return true;
                })
                .toList();

        return filtered.stream()
                .map(dancer -> new DancersFiltersResponse(
                        dancer.getAreas() != null && !dancer.getAreas().isEmpty()
                                ? dancer.getAreas().iterator().next().getId()
                                : null,
                        dancer.getDanceTypes().stream().map(DanceType::getId).toList(),
                        dancer.getPrice(),
                        dancer.getTeamSize(),
                        dancer.getDancerNickName(),
                        dancer.getDanceTypes().stream().map(DanceType::getType).collect(Collectors.toSet()),
                        dancer.getUser().getId(),
                        dancer.getYearExperience() != null ? dancer.getYearExperience() : 0, // ✅ fix null
                        dancer.getStatus() != null ? dancer.getStatus().getId() : null,
                        dancer.getId(),
                        dancer.getUser().getPhone()))
                .toList();
    }

    @Override
    public DancerResponse getDancerBySubscriptionStatus(Integer id) {
        return null;
    }

    @Override
    public DancerResponse getAllDancerBySubscriptionStatus() {
        return null;
    }
}
