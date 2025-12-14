package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.infrastructure.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());

        factory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());

        return builder
                .requestFactory(() -> factory)
                .build();
    }
}