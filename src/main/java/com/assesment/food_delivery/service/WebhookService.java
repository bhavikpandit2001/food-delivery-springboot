package com.assesment.food_delivery.service;

import com.assesment.food_delivery.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.time.LocalDateTime;

@Service
public class WebhookService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOrderStatusWebhook(Order order) {
        System.out.println("<===== WEBHOOK CALLED =====>");
        String webhookUrl = "http://localhost:8080/webhook/order-status"; // Customer-facing URL

        Map<String, Object> payload = Map.of(
                "orderId", order.getId(),
                "status", order.getStatus().toString(),
                "updatedAt", LocalDateTime.now().toString()
        );

        try {
            restTemplate.postForEntity(webhookUrl, payload, String.class);
            System.out.println("Webhook sent successfully for order: " + order.getId() + " status is " + order.getStatus());
        } catch (Exception e) {
            System.err.println("Failed to send webhook: " + e.getMessage());
        }
    }
}

