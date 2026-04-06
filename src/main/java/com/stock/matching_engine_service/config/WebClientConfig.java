package com.stock.matching_engine_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "orderServiceWebClient")
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean(name = "portfolioServiceWebClient")
    public WebClient portfolioServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8085")
                .build();
    }
}
