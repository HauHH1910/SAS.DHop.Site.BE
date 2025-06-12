package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.ChoreographerStatus;
import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.SubscriptionRepository;
import com.sas.dhop.site.service.ChoreographerService;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.ChoreographerMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Choreographer Service]")
public class ChoreographerServiceImpl implements ChoreographerService {

    private final ChoreographyRepository choreographyRepository;
    private final StatusService statusService;
    private final ChoreographerMapper choreographerMapper;
    private final DanceTypeService danceTypeService;
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Override
    public ChoreographerResponse updateChoreographer(
            Integer choreographyId, ChoreographerRequest choreographerRequest) {
        Choreography choreography = choreographyRepository
                .findById(choreographyId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        User curUser = userService.getLoginUser();
        if (!choreography.getUser().getId().equals(curUser.getId()))
            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);

        choreography.setAbout(choreographerRequest.about());
        choreography.setYearExperience(choreographerRequest.yearExperience());
        choreography.setPrice(choreographerRequest.price());

        if (choreographerRequest.danceTypeId() != null
                && !choreographerRequest.danceTypeId().isEmpty()) {
            Set<DanceType> danceTypes = new HashSet<>();
            for (Integer danceTypeId : choreographerRequest.danceTypeId()) {
                DanceType danceType = danceTypeService.findDanceType(danceTypeId);
                danceTypes.add(danceType);
            }
            choreography.setDanceTypes(danceTypes);
        }

        Choreography updateChoreography = choreographyRepository.save(choreography);
        return choreographerMapper.mapToChoreographerResponse(updateChoreography);
    }

    @Override
    public ChoreographerResponse removeChoreographer(Integer choreographyId) {
        Choreography choreography = choreographyRepository
                .findById(choreographyId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        Status inactiveStatus = statusService.getStatus(ChoreographerStatus.INACTIVE_CHOREOGRAPHER);
        choreography.setStatus(inactiveStatus);

        Choreography updateChoreographer = choreographyRepository.save(choreography);
        return choreographerMapper.mapToChoreographerResponse(updateChoreographer);
    }

    @Override
    public ChoreographerResponse getChoreographerById(Integer choreographyId) {
        Choreography choreography = choreographyRepository
                .findById(choreographyId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        return choreographerMapper.mapToChoreographerResponse(choreography);
    }

    @Override
    public List<ChoreographerResponse> getAllChoreography() {
        User currentUser = userService.getLoginUser();

        Set<RoleName> collect =
                currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        boolean isAdmin = collect.contains(RoleName.ADMIN);
        boolean isStaff = collect.contains(RoleName.STAFF);

        List<Choreography> choreographer;
        if (isStaff || isAdmin) {
            choreographer = choreographyRepository.findAll();
        } else {
            choreographer = choreographyRepository.findByStatus(
                    statusService.findStatusOrCreated(ChoreographerStatus.ACTIVATED_CHOREOGRAPHER));
        }

        return choreographer.stream()
                .map(choreographerMapper::mapToChoreographerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ChoreographerResponse getChoreographerBySubscriptionStatus(Integer id) {
        return null;
    }

    @Override
    public ChoreographerResponse getAllChoreographerBySubscriptionStatus() {
        return null;
    }
}
