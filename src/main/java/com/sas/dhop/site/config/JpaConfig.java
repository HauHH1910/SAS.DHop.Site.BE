package com.sas.dhop.site.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.sas.dhop.site.repository")
@EntityScan(basePackages = "com.sas.dhop.site.model")
public class JpaConfig {}
