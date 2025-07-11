package com.example.notificationservice.Repository;

import com.example.notificationservice.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByOrderNumber(String orderNumber);
}
