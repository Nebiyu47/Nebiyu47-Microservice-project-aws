package com.example.commom.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private String orderNumber;
    private List<OrderItemDto> orderItems;
    private String status;

}
