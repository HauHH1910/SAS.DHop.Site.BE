package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.response.UserGraphQLResponse;
import com.sas.dhop.site.dto.response.DancerGraphQLResponse;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.dto.response.ChoreographyGraphQLResponse;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.service.AreaService;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.model.DanceType;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.Performance;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.repository.DancerRepository;
import com.sas.dhop.site.repository.ChoreographyRepository;
import com.sas.dhop.site.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "[General GraphQL Controller]")
public class GeneralGraphQLController {

    private final UserRepository userRepository;
    private final DancerRepository dancerRepository;
    private final ChoreographyRepository choreographyRepository;
    private final AreaService areaService;
    private final DanceTypeService danceTypeService;
    private final StatusService statusService;
    private final PerformanceRepository performanceRepository;

    /**
     * Get all users with createdAt/updatedAt
     */
    @QueryMapping
    @Transactional(readOnly = true)
    public List<UserGraphQLResponse> users() {
        log.info("Fetching all users via GraphQL");
        return userRepository.findAll().stream()
                .map(UserGraphQLResponse::mapToUserGraphQLResponse)
                .toList();
    }

    /**
     * Get user by ID with createdAt/updatedAt
     */
    @QueryMapping
    public UserGraphQLResponse user(@Argument String id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(Integer.valueOf(id))
                .map(UserGraphQLResponse::mapToUserGraphQLResponse)
                .orElse(null);
    }

    /**
     * Get all dancers with createdAt/updatedAt
     */
    @QueryMapping
    public List<DancerGraphQLResponse> dancers() {
        log.info("Fetching all dancers via GraphQL");
        return dancerRepository.findAll().stream()
                .map(DancerGraphQLResponse::mapToDancerGraphQLResponse)
                .toList();
    }

    /**
     * Get dancer by ID with createdAt/updatedAt
     */
    @QueryMapping
    public DancerGraphQLResponse dancer(@Argument String id) {
        log.info("Fetching dancer with id: {}", id);
        return dancerRepository.findById(Integer.valueOf(id))
                .map(DancerGraphQLResponse::mapToDancerGraphQLResponse)
                .orElse(null);
    }

    /**
     * Get all areas
     */
    @QueryMapping
    public List<AreaResponse> areas() {
        log.info("Fetching all areas via GraphQL");
        return areaService.getAllArea();
    }

    /**
     * Get area by ID
     */
    @QueryMapping
    public AreaResponse area(@Argument String id) {
        log.info("Fetching area with id: {}", id);
        return areaService.getAreaById(Integer.valueOf(id));
    }

    /**
     * Get all dance types
     */
    @QueryMapping
    public List<DanceTypeResponse> danceTypes() {
        log.info("Fetching all dance types via GraphQL");
        return danceTypeService.getAllDanceType();
    }

    /**
     * Get dance type by ID
     */
    @QueryMapping
    public DanceTypeResponse danceType(@Argument String id) {
        log.info("Fetching dance type with id: {}", id);
        DanceType entity = danceTypeService.findDanceType(Integer.valueOf(id));
        return new DanceTypeResponse(entity.getId(), entity.getType(), entity.getDescription());
    }

    /**
     * Get all choreographies with createdAt/updatedAt
     */
    @QueryMapping
    public List<ChoreographyGraphQLResponse> choreographies() {
        log.info("Fetching all choreographies via GraphQL");
        return choreographyRepository.findAll().stream()
                .map(ChoreographyGraphQLResponse::mapToChoreographyGraphQLResponse)
                .toList();
    }

    /**
     * Get choreography by ID with createdAt/updatedAt
     */
    @QueryMapping
    public ChoreographyGraphQLResponse choreography(@Argument String id) {
        log.info("Fetching choreography with id: {}", id);
        return choreographyRepository.findById(Integer.valueOf(id))
                .map(ChoreographyGraphQLResponse::mapToChoreographyGraphQLResponse)
                .orElse(null);
    }

    /**
     * Get all performances
     */
    @QueryMapping
    public List<Performance> performances() {
        log.info("Fetching all performances via GraphQL");
        return performanceRepository.findAll();
    }

    /**
     * Get performance by ID
     */
    @QueryMapping
    public Performance performance(@Argument String id) {
        log.info("Fetching performance with id: {}", id);
        return performanceRepository.findById(Integer.valueOf(id)).orElse(null);
    }

    /**
     * Get all statuses
     */
    @QueryMapping
    public List<Status> statuses() {
        log.info("Fetching all statuses via GraphQL");
        // You'll need to implement this method in StatusService
        return List.of(); // Placeholder - implement this based on your StatusService
    }

    /**
     * Get status by ID
     */
    @QueryMapping
    public Status status(@Argument String id) {
        log.info("Fetching status with id: {}", id);
        // You'll need to implement this method in StatusService
        return null; // Placeholder - implement this based on your StatusService
    }
} 