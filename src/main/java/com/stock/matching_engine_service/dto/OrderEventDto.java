package com.stock.matching_engine_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stock.matching_engine_service.enums.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 🔥 prevents crash from extra fields
public class OrderEventDto {

    @NotNull
    private Long orderId;

    @NotNull
    private Long userId; 

    @NotBlank
    private String stockSymbol;

    @Min(1)
    private int quantity;

    @NotNull
    private BigDecimal price;

    @NotNull
    private OrderType orderType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
