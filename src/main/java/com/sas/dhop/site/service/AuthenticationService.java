package com.sas.dhop.site.service;

import com.nimbusds.jose.JOSEException;
import com.sas.dhop.site.dto.request.*;
import com.sas.dhop.site.dto.response.AuthenticationResponse;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import jakarta.mail.MessagingException;
import java.text.ParseException;

public interface AuthenticationService {

	void register(RegisterRequest request) throws MessagingException;

	AuthenticationResponse verifyOTPAndActiveUSer(VerifyOTPRequest request);

	AuthenticationResponse login(AuthenticationRequest request);

	IntrospectResponse introspect(IntrospectRequest request);

	AuthenticationResponse oauthLogin(String code);

	void forgotPassword(ForgotPasswordRequest request) throws MessagingException;

	AuthenticationResponse verifyOTPResetPassword(VerifyOTPRequest request);

	AuthenticationResponse resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException;

	AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

	boolean authenticationChecking(String role);
}
