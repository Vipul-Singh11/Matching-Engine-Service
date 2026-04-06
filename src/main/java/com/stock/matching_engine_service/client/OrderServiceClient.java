package com.stock.matching_engine_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class OrderServiceClient {

    private final WebClient webClient;

    public OrderServiceClient(@Qualifier("orderServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getOrderById(Long orderId) {
        return webClient.get()
                .uri("/api/orders/" + orderId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}