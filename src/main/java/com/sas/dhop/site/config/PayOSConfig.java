package com.sas.dhop.site.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {

  @Value("${sas.payos.client-id}")
  private String clientID;

  @Value("${sas.payos.api-key}")
  private String apiKey;

  @Value("${sas.payos.checksum-key}")
  private String checkSumKey;

  @Bean
  PayOS payOS() {
    return new PayOS(clientID, apiKey, checkSumKey);
  }
}
