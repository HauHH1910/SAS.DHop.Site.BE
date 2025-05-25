package com.sas.dhop.site.service.impl;

import com.nimbusds.jose.*;
import com.sas.dhop.site.controller.client.OAuthIdentityClient;
import com.sas.dhop.site.controller.client.OAuthUserClient;
import com.sas.dhop.site.dto.request.*;
import com.sas.dhop.site.dto.response.AuthenticationResponse;
import com.sas.dhop.site.dto.response.ExchangeTokenResponse;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import com.sas.dhop.site.dto.response.OAuthUserResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Role;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.RoleRepository;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.service.OTPService;
import com.sas.dhop.site.service.RoleService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.util.JwtUtil;
import jakarta.mail.MessagingException;

import java.text.ParseException;
import java.util.Set;

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

    private final OTPService oTPService;
    private final StatusService statusService;
    private final RoleService roleService;

    @NonFinal
    @Value("${sas.dhop.oauth.client-id}")
    private String CLIENT_ID;

    @NonFinal
    @Value("${sas.dhop.oauth.client-secret}")
    private String CLIENT_SECRET;

    @NonFinal
    @Value("${sas.dhop.oauth.redirect-uri}")
    private String REDIRECT_URI;

    @NonFinal
    @Value("${sas.dhop.oauth.grant-type}")
    private String GRANT_TYPE;

    @NonFinal
    @Value("${sas.dhop.site.valid-duration}")
    private long VALID_DURATION;

    @Value("${sas.dhop.site.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthUserClient userClient;
    private final OAuthIdentityClient identityClient;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(request.password(), user.getPassword());

        if (!authenticated) {
            throw new BusinessException(ErrorConstant.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        String refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);
        log.info("User {} login successfully", user.getEmail());

        return new AuthenticationResponse(accessToken, refreshToken);
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

    @Override
    public AuthenticationResponse oauthLogin(String code) {
        var response = getTokenResponse(code);

        log.debug("Received OAuth response for user login.");

        var userInfo = userClient.getUserInfo("json", response.accessToken());

        Role role = roleService.findByRoleName(RoleName.USER);

        Set<Role> roles = Set.of(role);

        var status = statusService.createStatus("Kích hoạt thành công");

        var user = onBoardUserOAuth(userInfo, status, roles);

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = jwtUtil.verifyToken(request.token(), true);

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.UNAUTHENTICATED));

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public void register(RegisterRequest request) throws MessagingException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException(ErrorConstant.EMAIL_ALREADY_EXIST);
        }

        Role role = roleService.findByRoleName(request.role());

        Set<Role> roles = Set.of(role);

        var status = statusService.createStatus("Chưa kích hoạt");

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .status(status)
                .build();

        userRepository.save(user);

        String generateOTP = oTPService.generateOTP(request.email());

        oTPService.sendOTPByEmail(request.email(), request.name(), generateOTP).thenAccept(success -> {
            if (success) {
                log.info("Sent OTP to email [{}]", request.email());
            } else {
                log.error("Sent OTP fail to email [{}]", request.email());
            }
        });
    }

    @Override
    public AuthenticationResponse verifyOTPAndActiveUSer(VerifyOTPRequest request) {
        boolean isValid = oTPService.validateOTP(request.email(), request.otpCode());
        if (!isValid) throw new BusinessException(ErrorConstant.INVALID_OTP);

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        var status = statusService.createStatus("Kích hoạt thành công");

        user.setStatus(status);
        userRepository.save(user);

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private ExchangeTokenResponse getTokenResponse(String code) {
        return identityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());
    }

    private User onBoardUserOAuth(OAuthUserResponse userInfo, Status status, Set<Role> roles) {
        return userRepository
                .findByEmail(userInfo.email())
                .orElseGet(() -> userRepository.save(User.builder()
                        .status(status)
                        .roles(roles)
                        .email(userInfo.email())
                        .name(String.join(" ", userInfo.givenName(), userInfo.familyName()))
                        .avatar(userInfo.picture())
                        .build()));
    }
}
