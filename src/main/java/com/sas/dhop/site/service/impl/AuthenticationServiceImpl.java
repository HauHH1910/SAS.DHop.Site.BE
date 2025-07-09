package com.sas.dhop.site.service.impl;

import static com.sas.dhop.site.constant.ChoreographerStatus.ACTIVATED_CHOREOGRAPHER;
import static com.sas.dhop.site.constant.DancerStatus.ACTIVATED_DANCER;
import static com.sas.dhop.site.constant.UserStatus.ACTIVE_USER;
import static com.sas.dhop.site.constant.UserStatus.INACTIVE_USER;

import com.nimbusds.jose.*;
import com.sas.dhop.site.controller.client.OAuthIdentityClient;
import com.sas.dhop.site.controller.client.OAuthUserClient;
import com.sas.dhop.site.dto.request.*;
import com.sas.dhop.site.dto.response.*;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.JwtUtil;
import com.sas.dhop.site.util.mapper.DancerMapper;
import com.sas.dhop.site.util.mapper.UserMapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Authentication Service]")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final OTPService oTPService;
    private final StatusService statusService;
    private final RoleService roleService;
    private final DanceTypeService danceTypeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthUserClient userClient;
    private final OAuthIdentityClient identityClient;
    private final JwtUtil jwtUtil;
    private final ChoreographyRepository choreographyRepository;
    private final DancerRepository dancerRepository;
    private final UserMapper userMapper;
    private final DancerMapper dancerMapper;
    private final AreaRepository areaRepository;

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

    @NonFinal
    @Value("${sas.dhop.site.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${sas.dhop.site.reset-duration}")
    private long RESET_DURATION;

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

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.token();

        boolean isValid = true;

        try {
            jwtUtil.verifyToken(token, VALID_DURATION, false);
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

        var status = statusService.findStatusOrCreated(ACTIVE_USER);

        var user = onBoardUserOAuth(userInfo, status, roles);

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws MessagingException {
        var user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
        log.info("[{}] quên mật khẩu rồi sir", request.email());

        String otp = oTPService.generateOTP(request.email());
        boolean success =
                oTPService.sendOTPByEmail(request.email(), user.getName(), otp).join();

        if (!success) {
            log.error("[forgot password] Gửi OTP thất bại cho email [{}]", request.email());
            throw new BusinessException(ErrorConstant.SENT_EMAIL_ERROR);
        }

        log.info("[forgot password] OTP đã được gửi tới [{}]", request.email());
    }

    @Override
    public AuthenticationResponse verifyOTPResetPassword(VerifyOTPRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        log.info("[Verify OTP reset password] - [{}]", request.otpCode());
        oTPService.validateOTP(user.getEmail(), request.otpCode());
        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    public AuthenticationResponse resetPassword(ResetPasswordRequest request) {
        boolean isValidToken = true;
        String email;

        try {
            var claims = jwtUtil.verifyToken(request.token(), RESET_DURATION, false);
            email = claims.getSubject();

            log.info("[{}] is trying to reset password sir", email);
        } catch (ParseException | JOSEException | BadJwtException e) {
            isValidToken = false;
            email = null;
        }

        if (!isValidToken || email == null) {
            throw new BusinessException(ErrorConstant.INVALID_TOKEN);
        }

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = jwtUtil.verifyToken(request.token(), REFRESHABLE_DURATION, true);

        var email = signedJWT.getSubject();

        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.UNAUTHENTICATED));

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        // Kiểm tra trùng email
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException(ErrorConstant.EMAIL_ALREADY_EXIST);
        }

        // Lấy status mặc định cho user mới
        Status userStatus = statusService.findStatusOrCreated(INACTIVE_USER);

        // Tìm role theo tên
        Role role = roleService.findByRoleName(request.role());
        Set<Role> roles = Set.of(role);

        // Tạo user entity
        User user = User.builder()
                .name(request.name())
                .phone(request.phone())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .status(userStatus)
                .build();

        userRepository.save(user);

        // Chỉ bắt buộc kiểm tra khu vực nếu role là DANCER hoặc CHOREOGRAPHY
        Set<Area> workAreas = new HashSet<>();
        boolean isDancerOrChoreo =
                RoleName.DANCER.equals(request.role()) || RoleName.CHOREOGRAPHY.equals(request.role());
        if (isDancerOrChoreo) {
            if (request.areaIds() == null || request.areaIds().isEmpty()) {
                throw new BusinessException(ErrorConstant.AREA_NOT_NULL);
            }
            List<Area> foundAreas = areaRepository.findAllById(request.areaIds());
            if (foundAreas.size() != request.areaIds().size()) {
                throw new BusinessException(ErrorConstant.AREA_NOT_FOUND);
            }
            workAreas = new HashSet<>(foundAreas);
        }

        // Nếu là CHOREOGRAPHY
        if (RoleName.CHOREOGRAPHY.equals(request.role()) && request.choreography() != null) {
            log.info("[{}] đăng ký vai trò CHOREOGRAPHY", request.email());

            Status choreographyStatus = statusService.findStatusOrCreated(ACTIVATED_CHOREOGRAPHER);

            Set<DanceType> danceTypes = request.choreography().danceType().stream()
                    .map(danceTypeService::findDanceType)
                    .collect(Collectors.toSet());

            Choreography choreography = Choreography.builder()
                    .danceTypes(danceTypes)
                    .user(user)
                    .about(request.choreography().about())
                    .yearExperience(request.choreography().yearExperience())
                    .status(choreographyStatus)
                    .areas(workAreas)
                    .build();

            choreographyRepository.save(choreography);
        }
        // Nếu là DANCER
        else if (RoleName.DANCER.equals(request.role()) && request.dancer() != null) {
            log.info("[{}] đăng ký vai trò DANCER", request.email());

            Status dancerStatus = statusService.findStatusOrCreated(ACTIVATED_DANCER);

            Set<DanceType> danceTypes = request.dancer().danceType().stream()
                    .map(danceTypeService::findDanceType)
                    .collect(Collectors.toSet());

            Dancer dancer = Dancer.builder()
                    .about(request.dancer().about())
                    .dancerNickName(request.dancer().dancerNickName())
                    .user(user)
                    .danceTypes(danceTypes)
                    .yearExperience(request.dancer().yearExperience())
                    .teamSize(request.dancer().teamSize())
                    .status(dancerStatus)
                    .areas(workAreas)
                    .build();

            dancerRepository.save(dancer);
        }

        // Gửi OTP qua email
        String otp = oTPService.generateOTP(request.email());
        boolean success =
                oTPService.sendOTPByEmail(request.email(), request.name(), otp).join();

        if (!success) {
            log.error("Gửi OTP thất bại cho email [{}]", request.email());
            throw new BusinessException(ErrorConstant.SENT_EMAIL_ERROR);
        }

        log.info("OTP đã được gửi tới [{}]", request.email());
    }

    @Override
    public AuthenticationResponse verifyOTPAndActiveUSer(VerifyOTPRequest request) {
        oTPService.validateOTP(request.email(), request.otpCode());

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        var status = statusService.findStatusOrCreated(ACTIVE_USER);

        user.setStatus(status);
        userRepository.save(user);

        var accessToken = jwtUtil.generateToken(user, VALID_DURATION, false);
        var refreshToken = jwtUtil.generateToken(user, REFRESHABLE_DURATION, true);

        return getAuthenticationResponse(accessToken, refreshToken, user);
    }

    @Override
    public boolean authenticationChecking(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().anyMatch(authority -> {
                    log.info("User has the required role: {}", authority.getAuthority());
                    return role.equals(authority.getAuthority());
                });
    }

    @Override
    public UserResponse getUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByEmail(email)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
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

    private AuthenticationResponse getAuthenticationResponse(String accessToken, String refreshToken, User user) {
        Set<RoleName> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (roleNames.contains(RoleName.DANCER)) {
            DancerResponse dancerResponse = dancerMapper.mapToDancerResponse(dancerRepository
                    .findByUser(user)
                    .orElseThrow(() -> new BusinessException(ErrorConstant.NOT_DANCER)));
            return new AuthenticationResponse(
                    accessToken,
                    refreshToken,
                    userMapper.mapToUserResponse(user),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                    dancerResponse);
        } else {
            return new AuthenticationResponse(
                    accessToken,
                    refreshToken,
                    userMapper.mapToUserResponse(user),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                    null);
        }
    }
}
