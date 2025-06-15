package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.RoleRequest;
import com.sas.dhop.site.dto.response.RoleResponse;
import com.sas.dhop.site.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@Tag(name = "Role Controller")
@Slf4j(topic = "[Role Controller]")
public class RoleController {

	private final RoleService roleService;

	@GetMapping
	public ResponseData<List<RoleResponse>> getAllRole() {
		return ResponseData.<List<RoleResponse>>builder().message(ResponseMessage.GET_ALL_ROLE)
				.data(roleService.getAll()).build();
	}

	@PostMapping
	public ResponseData<RoleResponse> createRole(@RequestBody RoleRequest request) {
		return ResponseData.<RoleResponse>builder().message(ResponseMessage.CREATE_ROLE)
				.data(roleService.createRole(request)).build();
	}

	@DeleteMapping("/{id}")
	public ResponseData<Void> deleteRole(@PathVariable("id") Integer id) {
		roleService.deleteRole(id);
		return ResponseData.<Void>builder().message(ResponseMessage.DELETE_ROLE).build();
	}
}
