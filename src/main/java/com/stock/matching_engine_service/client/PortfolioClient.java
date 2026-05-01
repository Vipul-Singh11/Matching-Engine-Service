package com.stock.matching_engine_service.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PortfolioClient {

    private final WebClient webClient;

    public PortfolioClient(@Qualifier("portfolioServiceWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void sendTrade(Object tradeEvent) {
        webClient.post()
                .uri("/api/portfolio/trade")
                .bodyValue(tradeEvent)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
