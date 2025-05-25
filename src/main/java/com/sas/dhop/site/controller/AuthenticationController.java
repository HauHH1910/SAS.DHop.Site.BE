package com.sas.dhop.site.controller;

import com.nimbusds.jose.JOSEException;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.*;
import com.sas.dhop.site.dto.response.AuthenticationResponse;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import com.sas.dhop.site.model.enums.ResponseMessage;
import com.sas.dhop.site.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
@Slf4j(topic = "[Authentication Controller]")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseData<AuthenticationResponse> oauthAuthenticate(@RequestParam("code") String code) {
        return ResponseData.<AuthenticationResponse>builder()
                .message(ResponseMessage.AUTHENTICATION_LOGIN.getMessage())
                .data(authenticationService.oauthLogin(code))
                .build();
    }

    @PostMapping("/register")
    public ResponseData<Void> register(@RequestBody RegisterRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseData.<Void>builder()
                .message(ResponseMessage.REGISTER_SUCCESS.getMessage())
                .build();
    }

    @PostMapping("/verify-token")
    public ResponseData<AuthenticationResponse> verifyOTP(@RequestBody VerifyOTPRequest request) {
        return ResponseData.<AuthenticationResponse>builder()
                .data(authenticationService.verifyOTPAndActiveUSer(request))
                .message(ResponseMessage.VERIFICATION_OTP_SUCCESS.getMessage())
                .build();
    }

    @PostMapping("/login")
    @Operation(description = "API to login to the system", summary = "API to login to the system")
    public ResponseData<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        log.info("User try to login: {}", request.email());
        return ResponseData.<AuthenticationResponse>builder()
                .message(ResponseMessage.AUTHENTICATION_LOGIN.getMessage())
                .data(authenticationService.login(request))
                .build();
    }

    @PostMapping("/introspect")
    public ResponseData<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ResponseData.<IntrospectResponse>builder()
                .message(ResponseMessage.INTROSPECT_TOKEN.getMessage())
                .data(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/refresh")
    public ResponseData<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ResponseData.<AuthenticationResponse>builder()
                .message(ResponseMessage.REFRESH_TOKEN.getMessage())
                .data(authenticationService.refreshToken(request))
                .build();
    }
}
