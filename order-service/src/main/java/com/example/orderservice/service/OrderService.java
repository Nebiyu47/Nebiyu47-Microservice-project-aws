package com.example.orderservice.service;

import com.example.commom.event.OrderEvent;

import com.example.commom.event.OrderItemDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;

import com.example.orderservice.repository.OrderRepository;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class OrderService {
   private final OrderRepository orderRepository;
   private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    @Transactional
    public Order placedOrder(List<OrderItemDto> orderItemDtos, String customerEmail) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setCustomerEmail(customerEmail);

        List<OrderItem> orderItemList = orderItemDtos.stream()
                .map(dto -> mapToOrderItem(dto, order)) // ✅ pass the order to each item
                .collect(Collectors.toList());

        order.setOrderItems(orderItemList);

        BigDecimal totalAmount = orderItemList.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order); // ✅ Will cascade insert items

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderNumber(savedOrder.getOrderNumber());
        orderEvent.setOrderItems(orderItemDtos);
        orderEvent.setOrderItem(savedOrder.getOrderTime());
        orderEvent.setStatus(savedOrder.getStatus());
        orderEvent.setTotalAmount(savedOrder.getTotalAmount());
        orderEvent.setCustomerEmail(savedOrder.getCustomerEmail());

        kafkaTemplate.send("order-created", orderEvent);

        return savedOrder;
    }

    private OrderItem mapToOrderItem(OrderItemDto dto, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuCode(dto.getSkuCode());
        orderItem.setPrice(dto.getPrice());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setOrder(order); // ✅ Set the parent order (required)
        return orderItem;
    }

}
