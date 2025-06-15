package com.sas.dhop.site.config;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "[CustomJWTDecoder]")
public class CustomJwtDecoder implements JwtDecoder {

	@Value("${sas.dhop.site.key}")
	private String key;

	private NimbusJwtDecoder nimbusJwtDecoder;

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			var signedJWT = SignedJWT.parse(token);

			var verifier = new MACVerifier(key.getBytes());

			boolean verified = signedJWT.verify(verifier);

			Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

			if (!verified || expirationDate.before(new Date())) {
				throw new BusinessException(ErrorConstant.INVALID_TOKEN);
			}

			if (nimbusJwtDecoder == null) {
				var keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
				nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(keySpec).macAlgorithm(MacAlgorithm.HS512).build();
			}

			return nimbusJwtDecoder.decode(token);

		} catch (Exception e) {
			log.error("{}", e.getMessage());
			throw new BusinessException(ErrorConstant.UNCATEGORIZED_ERROR);
		}
	}
}
