package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.DancerResponse;
import com.sas.dhop.site.service.DancerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Dancers")
@Tag(name = "[Dancers Controller]")
@Slf4j(topic = "[Dancers Controller]")
public class DancersController {

    private final DancerService dancerService;

    public ResponseData<List<DancerResponse>> getAllDancer(){
        return ResponseData.<List<DancerResponse>>builder()
                .message(ResponseMessage.GET_ALL_DANCERS)
                .data(dancerService.getallDancer())
                .build();
    }

}
