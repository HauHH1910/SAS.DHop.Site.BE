package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.CreateUserRequest;
import com.sas.dhop.site.dto.request.UpdateUserRequest;
import com.sas.dhop.site.dto.response.UserResponse;
import com.sas.dhop.site.model.enums.ResponseMessage;
import com.sas.dhop.site.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Controller")
@Slf4j(topic = "[User Controller]")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseData<List<UserResponse>> getAllUser() {
        return ResponseData.<List<UserResponse>>builder()
                .message(ResponseMessage.GET_ALL_USER.getMessage())
                .data(userService.getAllUser())
                .build();
    }

    @GetMapping("/info")
    public ResponseData<UserResponse> getUserInfo() {
        return ResponseData.<UserResponse>builder()
                .message(ResponseMessage.GET_USER_INFO.getMessage())
                .data(userService.getUserInfo())
                .build();
    }

    @PostMapping
    public ResponseData<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseData.<UserResponse>builder()
                .message(ResponseMessage.CREATE_USER.getMessage())
                .data(userService.createUser(request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseData.<Void>builder()
                .message(ResponseMessage.DELETE_USER.getMessage())
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseData<UserResponse> updateUser(
            @PathVariable("id") Integer id, @RequestBody UpdateUserRequest request) {
        return ResponseData.<UserResponse>builder()
                .message(ResponseMessage.UPDATE_USER.getMessage())
                .data(userService.updateUser(id, request))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<UserResponse> getUser(@PathVariable("id") Integer id) {
        return ResponseData.<UserResponse>builder()
                .message(ResponseMessage.GET_USER.getMessage())
                .data(userService.getUser(id))
                .build();
    }
}
