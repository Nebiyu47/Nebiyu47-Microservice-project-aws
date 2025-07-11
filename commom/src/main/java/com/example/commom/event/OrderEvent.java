package com.example.commom.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String orderNumber;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderItem;
    private String status;
    private BigDecimal totalAmount;
    private String customerEmail;


}
