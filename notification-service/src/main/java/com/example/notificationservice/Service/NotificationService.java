package com.example.notificationservice.Service;


import com.example.commom.event.InventoryEvent;
import com.example.notificationservice.Entity.Notification;
import com.example.commom.event.NotificationEvent;
import com.example.notificationservice.Repository.NotificationRepository;
import com.example.commom.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {


private final NotificationRepository notificationRepository;
private final KafkaTemplate<String , NotificationEvent> kafkaTemplate;

 @KafkaListener(topics="order-created",groupId = "notification-group")
    public void handleOrderCreatedEvent(OrderEvent orderEvent){
     String message = String.format("Thank you for your order #%s. We are processing it.",
             orderEvent.getOrderNumber());
     Notification notification = new Notification();
     notification.setOrderNumber(orderEvent.getOrderNumber());
     notification.setCustomerEmail(orderEvent.getCustomerEmail());
     notification.setMessage(message);
     notification.setSentAt(LocalDateTime.now());
     notification.setStatus("SENT");
     notificationRepository.save(notification);
     NotificationEvent notificationEvent = new NotificationEvent();
     notificationEvent.setOrderNumber(orderEvent.getOrderNumber());
     notificationEvent.setCustomerEmail(orderEvent.getCustomerEmail());
     notificationEvent.setMessage(message);
     kafkaTemplate.send("notification-sent",notificationEvent);
 }
 @KafkaListener(topics="inventory-checked", groupId = "notification-group")
    public void handleInventoryCheckedEvent(InventoryEvent inventoryEvent ) {

  String status = inventoryEvent.getStatus().equals("IN_STOCK") ? "Your order is confirmed and will be shipped soon." :
          "Some items in your order are out of stock. We'll notify you when they're available.";
  String message = String.format("Order #%s updated: %s",
          inventoryEvent.getOrderNumber(), status);
  Notification notification = new Notification();
  notification.setOrderNumber(inventoryEvent.getOrderNumber());
  notification.setCustomerEmail("nebiyugirma47@yahoo.com");
  notification.setMessage(message);
  notification.setSentAt(LocalDateTime.now());
  notification.setStatus("SENT");
  notificationRepository.save(notification);
  NotificationEvent notificationEvent = new NotificationEvent();
  notificationEvent.setOrderNumber(inventoryEvent.getOrderNumber());
  notificationEvent.setCustomerEmail("nebiyugirma47@yahoo.com");
  notificationEvent.setMessage(message);
  kafkaTemplate.send("notification-sent", notificationEvent);

 }
}
