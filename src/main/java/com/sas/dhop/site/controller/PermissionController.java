package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.PermissionRequest;
import com.sas.dhop.site.dto.response.PermissionResponse;
import com.sas.dhop.site.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@Tag(name = "Permission Controller")
@Slf4j(topic = "[Permission Controller]")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public ResponseData<List<PermissionResponse>> getAllRole() {
        return ResponseData.<List<PermissionResponse>>builder()
                .message(ResponseMessage.GET_ALL_ROLE)
                .data(permissionService.getAll())
                .build();
    }

    @PostMapping
    public ResponseData<PermissionResponse> createRole(@RequestBody PermissionRequest request) {
        return ResponseData.<PermissionResponse>builder()
                .message(ResponseMessage.CREATE_ROLE)
                .data(permissionService.createPermission(request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteRole(@PathVariable("id") Integer id) {
        permissionService.deletePermission(id);
        return ResponseData.<Void>builder().message(ResponseMessage.DELETE_ROLE).build();
    }
}
