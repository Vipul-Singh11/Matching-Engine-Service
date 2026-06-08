package com.stock.matching_engine_service.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.stock.matching_engine_service.dto.OrderExecutionDto;

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

    public void updateOrderExecution(
        Long orderId,
        Integer executedQuantity) {

        webClient.put()
                .uri("/api/orders/" + orderId + "/execution")
                .bodyValue(
                        new OrderExecutionDto(
                                executedQuantity))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
