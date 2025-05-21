package com.sas.dhop.site.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.ResponseError;
import com.sas.dhop.site.exception.ErrorConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j(topic = "[JWT AUTHENTICATION ENTRY POINT]")
class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorConstant errorConstant = ErrorConstant.UNAUTHENTICATED;

        response.setStatus(errorConstant.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseData<ResponseError> responseData = ResponseData.<ResponseError>builder()
                .code(errorConstant.getCode())
                .message(errorConstant.getMessage())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        log.info("JwtAuthenticationEntryPoint {}", mapper.writeValueAsString(responseData));

        response.getWriter().write(mapper.writeValueAsString(responseData));

        response.flushBuffer();
    }
}
