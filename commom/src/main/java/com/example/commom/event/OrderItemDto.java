package com.example.commom.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
