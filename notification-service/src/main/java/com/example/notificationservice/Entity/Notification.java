package com.example.notificationservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

         @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;
         private String orderNumber;
         private String customerEmail;
         private String message;
         private LocalDateTime sentAt;
         private String status;
}
