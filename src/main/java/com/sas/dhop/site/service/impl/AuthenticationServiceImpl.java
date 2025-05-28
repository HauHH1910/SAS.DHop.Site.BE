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
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.*;
import com.sas.dhop.site.service.*;
import com.sas.dhop.site.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final EmailService emailService;
    private final DanceTypeService danceTypeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthUserClient userClient;
    private final OAuthIdentityClient identityClient;
    private final JwtUtil jwtUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final ChoreographyRepository choreographyRepository;
    private final DancerRepository dancerRepository;

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
    public void forgotPassword(String email) throws MessagingException {
        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        String body = emailContent(user);

        emailService.sendEmail(email, "Đặt lại mật khẩu", body);
    }

    @Override
    public AuthenticationResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        boolean isValidToken = true;

        try {
            jwtUtil.verifyToken(request.token(), false);
        } catch (ParseException | JOSEException e) {
            isValidToken = false;
        }

        if (!isValidToken) {
            throw new BusinessException(ErrorConstant.INVALID_TOKEN);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

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
    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException(ErrorConstant.EMAIL_ALREADY_EXIST);
        }

        Status userStatus = statusService.createStatus("Chưa kích hoạt");

        Role role = roleService.findByRoleName(request.role());
        Set<Role> roles = Set.of(role);

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .status(userStatus)
                .build();

        userRepository.save(user);

        if (RoleName.CHOREOGRAPHY.equals(request.role()) && request.choreography() != null) {
            log.info("[{}] đăng ký vai trò CHOREOGRAPHY", request.email());
            Status choreographyStatus = statusService.createStatus("Chờ xác nhận để trở thành biên đạo");

            Set<DanceType> danceTypes = request.choreography().danceType().stream()
                    .map(danceTypeService::findDanceType)
                    .collect(Collectors.toSet());

            Choreography choreography = Choreography.builder()
                    .danceTypes(danceTypes)
                    .user(user)
                    .about(request.choreography().about())
                    .yearExperience(request.choreography().yearExperience())
                    .status(choreographyStatus)
                    .build();

            choreographyRepository.save(choreography);
        } else if (RoleName.DANCER.equals(request.role()) && request.dancer() != null) {
            log.info("[{}] đăng ký vai trò DANCER", request.email());
            Status dancerStatus = statusService.createStatus("Chờ xác nhận để trở thành nhóm nhảy");

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
                    .build();

            dancerRepository.save(dancer);
        }

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

    private String emailContent(User user) {
        String token = jwtUtil.generateToken(user, VALID_DURATION, false);

        String resetUrl = String.format("http://localhost:3000/reset-password?token=%s", token);

        return String.format(
                "<p>Chào bạn,</p>" + "<p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản của mình.</p>"
                        + "<p>Vui lòng bấm nút bên dưới để xác nhận và đặt lại mật khẩu:</p>"
                        + "<p style=\"text-align:center;\">"
                        + "<a href=\"%s\" style=\"display:inline-block; padding:10px 20px; font-size:16px; color:#fff; background-color:#007bff; text-decoration:none; border-radius:5px;\">Xác nhận reset mật khẩu</a>"
                        + "</p>"
                        + "<p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>",
                resetUrl);
    }
}
