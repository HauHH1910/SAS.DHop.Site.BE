package com.sas.dhop.site.service;

import com.nimbusds.jose.JOSEException;
import com.sas.dhop.site.dto.request.AuthenticationRequest;
import com.sas.dhop.site.dto.request.IntrospectRequest;
import com.sas.dhop.site.dto.request.RefreshTokenRequest;
import com.sas.dhop.site.dto.response.AuthenticationResponse;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import java.text.ParseException;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse oauthLogin(String code);

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
