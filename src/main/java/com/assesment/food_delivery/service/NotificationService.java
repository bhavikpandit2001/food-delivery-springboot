package com.assesment.food_delivery.service;

import com.assesment.food_delivery.enums.NotificationTypes;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(Long order_id, String message, NotificationTypes type){
        System.out.println("=== mock notification sent ===");
        System.out.println("Order id => "+ order_id);
        System.out.println("Type => "+ type);
        System.out.println("Message => "+ message);
    }
}
