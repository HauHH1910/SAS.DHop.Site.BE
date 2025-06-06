package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.ChoreographerStatus;
import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.DanceType;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.SubscriptionRepository;
import com.sas.dhop.site.service.ChoreographerService;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.ChoreographerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ChoreographerResponse updateChoreographer(Integer choreographyId, ChoreographerRequest choreographerRequest) {
        Choreography choreography = choreographyRepository
                .findById(choreographyId)
                .orElseThrow(()-> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        User curUser = userService.getLoginUser();
        if(!choreography.getUser().getId().equals(curUser.getId()))
            throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);

        choreography.setAbout(choreographerRequest.about());
        choreography.setYearExperience(choreographerRequest.yearExperience());
        choreography.setPrice(choreographerRequest.price());

        if(choreographerRequest.danceTypeId() != null && !choreographerRequest.danceTypeId().isEmpty()) {
            Set<DanceType> danceTypes = new HashSet<>();
            for(Integer danceTypeId : choreographerRequest.danceTypeId()) {
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
                .orElseThrow(()-> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        Status inactiveStatus = statusService.getStatus(ChoreographerStatus.INACTIVE_CHOREOGRAPHER);
        choreography.setStatus(inactiveStatus);

        Choreography updateChoreographer = choreographyRepository.save(choreography);
        return choreographerMapper.mapToChoreographerResponse(updateChoreographer);
    }

    @Override
    public ChoreographerResponse getChoreographerById(Integer choreographyId) {
        Choreography choreography = choreographyRepository
                .findById(choreographyId)
                .orElseThrow(()-> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        return choreographerMapper.mapToChoreographerResponse(choreography);
    }

    @Override
    public List<ChoreographerResponse> getAllChoreography() {
        User currentUser = userService.getLoginUser();
        boolean isStaff = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleName.STAFF));
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleName.ADMIN));

        List<Choreography> choreographies;
        if (isStaff || isAdmin) {
            choreographies = choreographyRepository.findAll();
        } else {
            Status activeStatus = statusService.getStatus(ChoreographerStatus.ACTIVATED_CHOREOGRAPHER);
            choreographies = getAllChoreographyByStatus(activeStatus);
        }

        return choreographies.stream()
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

    // Method to find Choreography by status
    private List<Choreography> getAllChoreographyByStatus(Status status) {
        List<Choreography> allChoreography = choreographyRepository.findAll();
        return allChoreography.stream()
                .filter(choreography -> 
                    choreography.getStatus() != null && choreography.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
