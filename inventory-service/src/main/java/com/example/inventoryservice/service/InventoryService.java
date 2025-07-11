package com.example.inventoryservice.service;

import com.example.commom.event.InventoryEvent;
import com.example.inventoryservice.entity.Inventory;

import com.example.inventoryservice.repository.InventoryRepository;

import com.example.commom.event.OrderEvent;
import com.example.commom.event.OrderItemDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public InventoryService(InventoryRepository inventoryRepository, KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics="order-created",groupId = "inventory-group")
    @Transactional
    public void handleOrderCreatedEvent(OrderEvent orderEvent){
        List<String> skuCodes =orderEvent.getOrderItems().stream()
                .map(OrderItemDto::getSkuCode)
                .collect(Collectors.toList());
        List<Inventory>inventories =inventoryRepository.findBySkuCodeIn(skuCodes);
        boolean allInStocks= orderEvent.getOrderItems().stream()
                .allMatch(orderItem->inventories.stream()
                        .anyMatch(inventory ->inventory.getSkuCode().equals(orderItem.getSkuCode())
                        &&inventory.getQuantity()>=orderItem.getQuantity()));
        InventoryEvent inventoryEvent= new InventoryEvent();
        inventoryEvent.setOrderNumber(orderEvent.getOrderNumber());
        inventoryEvent.setOrderItems(orderEvent.getOrderItems());
        inventoryEvent.setStatus(allInStocks?"IN_STOCK":"OUT_OF_STOCK");
        kafkaTemplate.send("inventory-checked",inventoryEvent);
        if(allInStocks){
            orderEvent.getOrderItems().forEach(orderItem->{
                Inventory inventory=inventories.stream()
                        .filter(i->i.getSkuCode().equals(orderItem.getSkuCode()))
                        .findFirst()
                        .orElseThrow();
                inventory.setQuantity(inventory.getQuantity()- orderItem.getQuantity());
            });
            inventoryRepository.saveAll(inventories);
        }
    }


    }
