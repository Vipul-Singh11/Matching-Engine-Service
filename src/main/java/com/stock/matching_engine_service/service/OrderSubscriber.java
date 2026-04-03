package com.stock.matching_engine_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.matching_engine_service.dto.OrderEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSubscriber implements MessageListener {

    private final MatchingEngineService matchingEngineService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = new String(message.getBody());

            OrderEventDto order = objectMapper.readValue(msg, OrderEventDto.class);

            log.info("Received order from Redis: {}", order);

            matchingEngineService.processOrder(order);

        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}