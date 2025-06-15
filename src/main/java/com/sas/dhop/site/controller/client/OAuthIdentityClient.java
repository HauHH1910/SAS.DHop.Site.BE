package com.sas.dhop.site.controller.client;

import com.sas.dhop.site.dto.request.ExchangeTokenRequest;
import com.sas.dhop.site.dto.response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "oauth-identity-client", url = "https://oauth2.googleapis.com")
public interface OAuthIdentityClient {

  @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
