package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.AreaRequest;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.service.AreaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/area")
@Tag(name = "[Area Controller]")
@Slf4j(topic = "[Area Controller]")
public class AreaController {
	private final AreaService areaService;

	@GetMapping
	public ResponseData<List<AreaResponse>> getAllArea() {
		return ResponseData.<List<AreaResponse>>builder().message(ResponseMessage.GET_ALL_AREA)
				.data(areaService.getAllArea()).build();
	}

	@PostMapping
	public ResponseData<AreaResponse> createArea(@RequestBody AreaRequest request) {
		return ResponseData.<AreaResponse>builder().message(ResponseMessage.CREATE_AREA)
				.data(areaService.createNewArea(request)).build();
	}
}
