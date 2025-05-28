package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.DanceTypeRequest;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.model.enums.ResponseMessage;
import com.sas.dhop.site.service.DanceTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dance-type")
@Tag(name = "Dance Type Controller")
@Slf4j(topic = "[Dance Type Controller]")
public class DanceTypeController {

    private final DanceTypeService danceTypeService;

    @GetMapping
    public ResponseData<List<DanceTypeResponse>> getAllDanceType() {
        return ResponseData.<List<DanceTypeResponse>>builder()
                .message(ResponseMessage.GET_ALL_DANCE_TYPE)
                .data(danceTypeService.getAllDanceType())
                .build();
    }

    @PostMapping
    public ResponseData<DanceTypeResponse> createDanceType(@RequestBody DanceTypeRequest request) {
        return ResponseData.<DanceTypeResponse>builder()
                .message(ResponseMessage.CREATE_DANCE_TYPE)
                .data(danceTypeService.createDanceType(request))
                .build();
    }
}
