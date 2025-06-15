package com.sas.dhop.site.controller.client;

import com.sas.dhop.site.dto.response.OAuthUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oauth-user-client", url = "https://www.googleapis.com")
public interface OAuthUserClient {

  @GetMapping("/oauth2/v1/userinfo")
  OAuthUserResponse getUserInfo(
      @RequestParam("alt") String alt, @RequestParam("access_token") String accessToken);
}
