package com.stock.matching_engine_service.controller;

import com.stock.matching_engine_service.dto.OrderEventDto;
import com.stock.matching_engine_service.dto.TradeResponseDto;
import com.stock.matching_engine_service.service.MatchingEngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matching-engine")
@RequiredArgsConstructor
public class MatchingEngineController {

    private static final Logger log = LoggerFactory.getLogger(MatchingEngineController.class);

    private final MatchingEngineService matchingEngineService;

    @PostMapping("/match")
    public ResponseEntity<List<TradeResponseDto>> matchOrder(
            @Valid @RequestBody OrderEventDto order) {

        log.info("Received order for matching: {}", order);

        List<TradeResponseDto> trades = matchingEngineService.processOrder(order);

        log.info("Order processed. Trades executed: {}", trades.size());

        return ResponseEntity.ok(trades);
    }
}