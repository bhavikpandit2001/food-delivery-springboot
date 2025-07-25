package com.assesment.food_delivery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/order-status")
    public ResponseEntity<String> handleOrderStatusWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook received: " + payload);

        // Optional: handle logic here
        return ResponseEntity.ok("Webhook received successfully");
    }
}
