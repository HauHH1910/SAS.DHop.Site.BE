package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.DancerStatus;
import com.sas.dhop.site.dto.request.DancerRequest;
import com.sas.dhop.site.dto.response.DancerResponse;
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
    private final StatusRepository statusRepository;
    private final StatusService statusService;
    private final SubscriptionRepository subscriptionRepository;
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
        if(danceType == null ) throw new BusinessException(ErrorConstant.NOT_FOUND_DANCE_TYPE);

        List<Dancer> allDancers = dancerRepository.findAll();

        for(Dancer dancer : allDancers){
            if(dancer.getDanceTypes() != null && dancer.getDanceTypes().contains(danceType)){
                return dancerMapper.mapToDancerResponse(dancer);
            }
        }
        throw new BusinessException(ErrorConstant.USER_NOT_FOUND);
    }

    @Override
    public List<DancerResponse> getallDancer() {
        User currentUser = userService.getLoginUser();
        boolean isStaff =
                currentUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.STAFF));

        boolean isAdmin =
                currentUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ADMIN));

        List<Dancer> dancers;
        if (isStaff || isAdmin) {
            dancers = dancerRepository.findAll();
        } else {
            Status activeStatus = statusService.getStatus(DancerStatus.ACTIVATED_DANCER);
            dancers = getAllDancerByStatus(activeStatus);
        }

        return dancers.stream().map(dancerMapper::mapToDancerResponse).collect(Collectors.toList());
    }

    @Override
    public DancerResponse getDancerBySubscriptionStatus(Integer id) {
        // TODO: Implement getting dancer by subscription status
        return null;
    }

    @Override
    public DancerResponse getAllDancerBySubscriptionStatus() {
        // TODO: Implement getting all dancers by subscription status
        return null;
    }

    // Method to find Dancer by status
    public List<Dancer> getAllDancerByStatus(Status status) {
        List<Dancer> allDancer = dancerRepository.findAll();
        return allDancer.stream()
                .filter(dancer ->
                        dancer.getStatus() != null && dancer.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
