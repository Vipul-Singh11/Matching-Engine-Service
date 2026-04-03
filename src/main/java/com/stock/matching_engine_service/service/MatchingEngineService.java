package com.stock.matching_engine_service.service;

import com.stock.matching_engine_service.dto.OrderEventDto;
import com.stock.matching_engine_service.dto.TradeResponseDto;

import java.util.List;

public interface MatchingEngineService {

    List<TradeResponseDto> processOrder(OrderEventDto order);
}