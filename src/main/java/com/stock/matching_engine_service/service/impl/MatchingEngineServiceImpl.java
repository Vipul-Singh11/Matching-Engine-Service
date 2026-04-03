package com.stock.matching_engine_service.service.impl;

import com.stock.matching_engine_service.dto.OrderEventDto;
import com.stock.matching_engine_service.dto.TradeResponseDto;
import com.stock.matching_engine_service.entity.Trade;
import com.stock.matching_engine_service.enums.OrderType;
import com.stock.matching_engine_service.service.MatchingEngineService;
import com.stock.matching_engine_service.util.BuyOrderComparator;
import com.stock.matching_engine_service.util.SellOrderComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MatchingEngineServiceImpl implements MatchingEngineService {

    // 🔥 Order books per stock (VERY IMPORTANT DESIGN FIX)
    private final Map<String, PriorityQueue<OrderEventDto>> buyOrderBook = new HashMap<>();
    private final Map<String, PriorityQueue<OrderEventDto>> sellOrderBook = new HashMap<>();

    @Override
    public List<TradeResponseDto> processOrder(OrderEventDto order) {

        log.info("Processing order: {}", order);

        List<TradeResponseDto> trades = new ArrayList<>();

        String symbol = order.getStockSymbol();

        // Initialize order books if not present
        buyOrderBook.putIfAbsent(symbol, new PriorityQueue<>(new BuyOrderComparator()));
        sellOrderBook.putIfAbsent(symbol, new PriorityQueue<>(new SellOrderComparator()));

        PriorityQueue<OrderEventDto> buyOrders = buyOrderBook.get(symbol);
        PriorityQueue<OrderEventDto> sellOrders = sellOrderBook.get(symbol);

        // Step 1: Add order to correct book
        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }

        // Step 2: Matching logic
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {

            OrderEventDto buy = buyOrders.peek();
            OrderEventDto sell = sellOrders.peek();

            // 🔥 BigDecimal comparison
            if (buy.getPrice().compareTo(sell.getPrice()) >= 0) {

                int executedQty = Math.min(buy.getQuantity(), sell.getQuantity());

                // Market convention → sell price
                BigDecimal executedPrice = sell.getPrice();

                // Create Trade
                Trade trade = Trade.builder()
                        .id(System.currentTimeMillis()) // simple unique ID
                        .buyOrderId(buy.getOrderId())
                        .sellOrderId(sell.getOrderId())
                        .stockSymbol(symbol)
                        .quantity(executedQty)
                        .executedPrice(executedPrice)
                        .executionTime(LocalDateTime.now())
                        .build();

                log.info("Trade executed: {}", trade);

                trades.add(mapToDto(trade));

                // Update quantities
                buy.setQuantity(buy.getQuantity() - executedQty);
                sell.setQuantity(sell.getQuantity() - executedQty);

                // Remove completed orders
                if (buy.getQuantity() == 0) buyOrders.poll();
                if (sell.getQuantity() == 0) sellOrders.poll();

            } else {
                break;
            }
        }

        return trades;
    }

    private TradeResponseDto mapToDto(Trade trade) {
        return TradeResponseDto.builder()
                .tradeId(trade.getId())
                .buyOrderId(trade.getBuyOrderId())
                .sellOrderId(trade.getSellOrderId())
                .stockSymbol(trade.getStockSymbol())
                .quantity(trade.getQuantity())
                .price(trade.getExecutedPrice())
                .executionTime(trade.getExecutionTime())
                .build();
    }
}