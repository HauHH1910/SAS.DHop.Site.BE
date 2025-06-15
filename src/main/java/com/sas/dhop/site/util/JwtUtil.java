package com.sas.dhop.site.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.User;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class JwtUtil {

  @Value("${sas.dhop.site.key}")
  private String KEY;

  public String generateToken(User user, Long expireIn, Boolean isRefreshToken) {
    JWTClaimsSet claimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getEmail())
            .issuer("sas.dhop.site")
            .issueTime(new Date())
            .expirationTime(
                new Date(Instant.now().plus(expireIn, ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString())
            .claim("scope", buildScope(user))
            .claim("type", isRefreshToken ? "refresh" : "access")
            .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claimsSet);

    try {
      signedJWT.sign(new MACSigner(KEY.getBytes(StandardCharsets.UTF_8)));
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }

    return signedJWT.serialize();
  }

  public JWTClaimsSet verifyToken(String token, Long durationInSeconds, boolean isRefresh)
      throws ParseException, JOSEException {
    JWSVerifier jwsVerifier = new MACVerifier(KEY.getBytes(StandardCharsets.UTF_8));
    SignedJWT signedJWT = SignedJWT.parse(token);
    if (!signedJWT.verify(jwsVerifier)) {
      throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
    }

    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

    Date now = new Date();

    if (isRefresh) {
      Date issueTime = claims.getIssueTime();
      if (issueTime == null) {
        throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
      }
      Date expiryTime =
          Date.from(issueTime.toInstant().plus(durationInSeconds, ChronoUnit.SECONDS));

      if (now.after(expiryTime)) {
        throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
      }

    } else {
      Date expirationTime = claims.getExpirationTime();
      if (expirationTime == null || now.after(expirationTime)) {
        throw new BusinessException(ErrorConstant.UNAUTHENTICATED);
      }
    }

    return claims;
  }

  private String buildScope(User user) {
    StringJoiner joiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles()
          .forEach(
              role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                  role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
              });
    }
    return joiner.toString();
  }
}
