package com.sas.dhop.site;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@EnableAsync
@EnableFeignClients
@SpringBootApplication
@Slf4j(topic = "[Application]")
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        Environment env = app.run(args).getEnvironment();
        log.info("Application '{}' is running on port(s): {}",
                env.getProperty("spring.application.name"),
                Arrays.toString(env.getActiveProfiles()));
    }
}
