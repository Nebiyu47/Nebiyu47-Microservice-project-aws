package com.example.orderservice.controller;


import com.example.commom.event.OrderItemDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody List<OrderItemDto> orderItemDtos,
            @RequestHeader("X-User-Email") String customerEmail) {
        Order order = orderService.placedOrder(orderItemDtos, customerEmail);
        return ResponseEntity.ok(order);


    }
    }
