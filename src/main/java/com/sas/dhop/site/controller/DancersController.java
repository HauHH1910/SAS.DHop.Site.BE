package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.DancerRequest;
import com.sas.dhop.site.dto.request.DancersFiltersRequest;
import com.sas.dhop.site.dto.response.DancerResponse;
import com.sas.dhop.site.dto.response.DancersFiltersResponse;
import com.sas.dhop.site.service.DancerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dancers")
@Tag(name = "[Dancers Controller]")
@Slf4j(topic = "[Dancers Controller]")
public class DancersController {

    private final DancerService dancerService;

    @GetMapping("/get-all-dancers")
    public ResponseData<List<DancerResponse>> getAllDancer() {
        return ResponseData.<List<DancerResponse>>builder()
                .message(ResponseMessage.GET_ALL_DANCERS)
                .data(dancerService.getallDancer())
                .build();
    }

    @PatchMapping("/update-dancer-profile/{dancerId}")
    public ResponseData<DancerResponse> updateDancer(
            @PathVariable Integer dancerId, @RequestBody DancerRequest dancerRequest) {
        return ResponseData.<DancerResponse>builder()
                .message(ResponseMessage.UPDATE_DANCER)
                .data(dancerService.updateDancer(dancerId, dancerRequest))
                .build();
    }

    @DeleteMapping("/remove-dancers/{dancerId}")
    public ResponseData<DancerResponse> removeDancer(@PathVariable Integer dancerId) {
        return ResponseData.<DancerResponse>builder()
                .message(ResponseMessage.REMOVE_DANCER)
                .data(dancerService.removeDancer(dancerId))
                .build();
    }

    @GetMapping("/get-dancer-by-Id/{dancerId}")
    public ResponseData<DancerResponse> getDancerById(@PathVariable Integer dancerId) {
        return ResponseData.<DancerResponse>builder()
                .message(ResponseMessage.GET_DANCER)
                .data(dancerService.getDancerById(dancerId))
                .build();
    }

    @GetMapping("/get-dancer-by-dance-type/{danceTypeId}")
    public ResponseData<DancerResponse> getDancerByDanceType(@PathVariable Integer danceTypeId) {
        return ResponseData.<DancerResponse>builder()
                .message(ResponseMessage.GET_DANCER_BY_DANCE_TYPE)
                .data(dancerService.getDancerByDanceType(danceTypeId))
                .build();
    }

    //    @PutMapping("/filter-dancer")
    //    public ResponseData<List<DancersFiltersResponse>> filterDancers(@RequestBody DancersFiltersRequest
    // dancersFiltersRequest) {
    //        return ResponseData.<List<DancersFiltersResponse>>builder()
    //                .message(ResponseMessage.FILTERS_DANCERS_COMPLETE)
    //                .data(dancerService.getAllDancersFilters(dancersFiltersRequest))
    //                .build();
    //    }

    @GetMapping("/filter-dancer")
    public ResponseData<List<DancersFiltersResponse>> filterDancers(
            @RequestParam(required = false) Integer areaId,
            @RequestParam(required = false) List<Integer> danceTypeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer teamSize) {
        DancersFiltersRequest request = DancersFiltersRequest.builder()
                .areaId(areaId)
                .danceTypeId(danceTypeId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .teamSize(teamSize)
                .build();

        return ResponseData.<List<DancersFiltersResponse>>builder()
                .message(ResponseMessage.FILTERS_DANCERS_COMPLETE)
                .data(dancerService.getAllDancersFilters(request))
                .build();
    }
}
