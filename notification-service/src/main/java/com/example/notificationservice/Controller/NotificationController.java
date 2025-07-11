package com.example.notificationservice.Controller;

import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;




    @GetMapping
    public ResponseEntity<List<Notification>>getAllNotifications(){
        return ResponseEntity.ok(notificationRepository.findAll());
    }
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<List<Notification>> getNotificationsByOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(notificationRepository.findByOrderNumber(orderNumber));
    }

}
