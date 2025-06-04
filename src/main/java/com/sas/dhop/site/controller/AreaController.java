package com.sas.dhop.site.controller;


import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.model.enums.ResponseMessage;
import com.sas.dhop.site.service.AreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.DoubleStream.builder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Area")
@Tag(name = "[Area Controller]")
@Slf4j(topic = "[Area Controller]")
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    public ResponseData<List<AreaResponse>> getAllArea() {
        return ResponseData.<List<AreaResponse>>builder()
                .message(ResponseMessage.GET_ALL_AREA)
                .data(areaService.getAllArea())
                .build();
    }




}
