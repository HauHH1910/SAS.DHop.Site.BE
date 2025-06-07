package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import com.sas.dhop.site.service.ChoreographerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/choreographer")
@Tag(name = "[Choreographer Controller]")
@Slf4j(topic = "[Choreographer Controller]")
public class ChoreographerController {

    private final ChoreographerService choreographerService;

    @GetMapping("/get-all-choreographer")
    public ResponseData<List<ChoreographerResponse>> getAllChoreographer() {
        return ResponseData.<List<ChoreographerResponse>>builder()
                .message(ResponseMessage.GET_ALL_CHOREOGRAPHER)
                .data(choreographerService.getAllChoreography())
                .build();
    }


    public ResponseData<ChoreographerResponse> updateChoreographer(
            @PathVariable Integer choreographerId, @RequestBody ChoreographerRequest choreographerRequest
            ) {
        return ResponseData.<ChoreographerResponse>builder()
                .message(ResponseMessage.UPDATE_CHOREOGRAPHER)
                .data(choreographerService.updateChoreographer(choreographerId, choreographerRequest))
                .build();
    }


}
