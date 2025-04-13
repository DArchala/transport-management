package com.mxkoo.transport_management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Clock;

@Configuration
public class BeanConfiguration {

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                         .build();
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

}
