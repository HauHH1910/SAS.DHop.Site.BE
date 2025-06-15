package com.sas.dhop.site.config.ws;

import com.sas.dhop.site.config.CustomJwtDecoder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final CustomJwtDecoder customJwtDecoder;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)
            throws Exception {
        if (request instanceof ServletServerHttpRequest serverHttpRequest) {
            String token = serverHttpRequest.getServletRequest().getParameter("token");

            if (token != null && !token.isBlank()) {
                try {
                    Jwt jwt = customJwtDecoder.decode(token);
                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt);
                    attributes.put("auth", authenticationToken);
                    return true;
                } catch (JwtException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
