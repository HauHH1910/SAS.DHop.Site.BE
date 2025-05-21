package com.sas.dhop.site.service.impl;

import com.nimbusds.jose.*;
import com.sas.dhop.site.dto.request.IntrospectRequest;
import com.sas.dhop.site.dto.request.LoginRequest;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import com.sas.dhop.site.dto.response.LoginResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.util.JwtUtil;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Authentication Service]")
public class AuthenticationServiceImpl implements AuthenticationService {

    @NonFinal
    @Value("${sas.dhop.site.key}")
    private String KEY;

    @NonFinal
    @Value("${sas.dhop.site.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${sas.dhop.site.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(request.password(), user.getPassword());

        if (!authenticated) {
            throw new BusinessException(ErrorConstant.PASSWORD_NOT_MATCH);
        }

        String token = jwtUtil.generateToken(user);
        log.info("User {} login successfully", user.getEmail());

        return new LoginResponse(true, token);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.token();

        boolean isValid = true;

        try {
            jwtUtil.verifyToken(token, false);
        } catch (BusinessException | ParseException | JOSEException e) {
            isValid = false;
        }
        return new IntrospectResponse(isValid);
    }
}
