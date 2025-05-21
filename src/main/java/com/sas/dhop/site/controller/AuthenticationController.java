package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.LoginRequest;
import com.sas.dhop.site.dto.response.LoginResponse;
import com.sas.dhop.site.enums.ResponseMessage;
import com.sas.dhop.site.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
@Slf4j(topic = "[Authentication Controller]")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(description = "API to login to the system", summary = "API to login to the system")
    public ResponseData<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("User try to login: {}", request.email());
        return ResponseData.<LoginResponse>builder()
                .message(ResponseMessage.AUTHENTICATION_LOGIN.getMessage())
                .data(authenticationService.login(request))
                .build();
    }
}
