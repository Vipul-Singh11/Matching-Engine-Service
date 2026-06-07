package com.stock.matching_engine_service.controller;

import com.stock.matching_engine_service.dto.OrderEventDto;
import com.stock.matching_engine_service.dto.TradeResponseDto;
import com.stock.matching_engine_service.service.MatchingEngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matching-engine")
@RequiredArgsConstructor
public class MatchingEngineController {

    private final MatchingEngineService matchingEngineService;

    @PostMapping("/match")
    public ResponseEntity<List<TradeResponseDto>> matchOrder(
            @Valid @RequestBody OrderEventDto order) {

        return ResponseEntity.ok(matchingEngineService.processOrder(order));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId) {

        matchingEngineService.cancelOrder(orderId);

        return ResponseEntity.ok(
                "Order removed from matching engine");
    }
}
