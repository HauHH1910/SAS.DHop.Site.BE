package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.IntrospectRequest;
import com.sas.dhop.site.dto.request.LoginRequest;
import com.sas.dhop.site.dto.response.IntrospectResponse;
import com.sas.dhop.site.dto.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

    IntrospectResponse introspect(IntrospectRequest request);
}
