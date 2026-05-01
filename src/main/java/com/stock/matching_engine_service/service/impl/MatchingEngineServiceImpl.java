package com.stock.matching_engine_service.service.impl;

import com.stock.matching_engine_service.client.PortfolioClient;
import com.stock.matching_engine_service.dto.OrderEventDto;
import com.stock.matching_engine_service.dto.TradeResponseDto;
import com.stock.matching_engine_service.entity.Trade;
import com.stock.matching_engine_service.enums.OrderType;
import com.stock.matching_engine_service.service.MatchingEngineService;
import com.stock.matching_engine_service.util.BuyOrderComparator;
import com.stock.matching_engine_service.util.SellOrderComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingEngineServiceImpl implements MatchingEngineService {

    private final Map<String, PriorityQueue<OrderEventDto>> buyOrderBook = new HashMap<>();
    private final Map<String, PriorityQueue<OrderEventDto>> sellOrderBook = new HashMap<>();

    private final PortfolioClient portfolioClient;

    @Override
    public List<TradeResponseDto> processOrder(OrderEventDto order) {

        if (order.getTimestamp() == null) {
            order.setTimestamp(LocalDateTime.now());
        }

        log.info("Processing order: {}", order);

        List<TradeResponseDto> trades = new ArrayList<>();
        String symbol = order.getStockSymbol();

        buyOrderBook.putIfAbsent(symbol, new PriorityQueue<>(new BuyOrderComparator()));
        sellOrderBook.putIfAbsent(symbol, new PriorityQueue<>(new SellOrderComparator()));

        PriorityQueue<OrderEventDto> buyOrders = buyOrderBook.get(symbol);
        PriorityQueue<OrderEventDto> sellOrders = sellOrderBook.get(symbol);

        // Step 1: Add order
        if (order.getOrderType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }

        // Step 2: Matching
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {

            OrderEventDto buy = buyOrders.peek();
            OrderEventDto sell = sellOrders.peek();

            if (buy.getPrice().compareTo(sell.getPrice()) >= 0) {

                int executedQty = Math.min(buy.getQuantity(), sell.getQuantity());
                BigDecimal executedPrice = sell.getPrice();

                // 🔥 CREATE TRADE
                Trade trade = Trade.builder()
                        .id(System.currentTimeMillis())
                        .buyOrderId(buy.getOrderId())
                        .sellOrderId(sell.getOrderId())
                        .stockSymbol(symbol)
                        .quantity(executedQty)
                        .executedPrice(executedPrice)
                        .executionTime(LocalDateTime.now())
                        .build();

                log.info("Trade executed: {}", trade);

                TradeResponseDto tradeDto = mapToDto(trade);
                trades.add(tradeDto);

                // 🔥 DIRECT USER IDs (NO API CALL)
                Long buyerUserId = buy.getUserId();
                Long sellerUserId = sell.getUserId();

                // 🔥 SEND TO PORTFOLIO
                try {
                    Map<String, Object> tradeEvent = new HashMap<>();
                    tradeEvent.put("tradeId", tradeDto.getTradeId());
                    tradeEvent.put("buyerUserId", buyerUserId);
                    tradeEvent.put("sellerUserId", sellerUserId);
                    tradeEvent.put("stockSymbol", tradeDto.getStockSymbol());
                    tradeEvent.put("quantity", tradeDto.getQuantity());
                    tradeEvent.put("price", tradeDto.getPrice());

                    log.info("🔥 Sending trade to Portfolio: {}", tradeEvent);

                    portfolioClient.sendTrade(tradeEvent);

                    log.info("✅ Trade sent successfully to Portfolio");

                    log.info("Trade sent to Portfolio Service: {}", tradeDto.getTradeId());

                } catch (Exception e) {
                    log.error("Failed to send trade to Portfolio: {}", e.getMessage());
                }

                // Update quantities
                buy.setQuantity(buy.getQuantity() - executedQty);
                sell.setQuantity(sell.getQuantity() - executedQty);

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
