package com.example.springProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
@Configuration
@EnableScheduling
public class BatchConfig {

    @Value("${redmine.api.timeout:30000}")
    private int apiTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(apiTimeout))
                .setReadTimeout(Duration.ofMillis(apiTimeout))
                .build();
    }
}