package com.assesment.food_delivery.scheduler;

import com.assesment.food_delivery.entity.Order;
import com.assesment.food_delivery.enums.NotificationTypes;
import com.assesment.food_delivery.repository.OrderRepository;
import com.assesment.food_delivery.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkDelayedOrders(){

        try{
            System.out.println("=== delayed order scheduler working ===");
            LocalDateTime now = LocalDateTime.now();

            //cooking delayed
            LocalDateTime cookingThreshold = now.minusMinutes(20);
            List<Order> cookingDelayed = orderRepository.findCookingDelayed(cookingThreshold);

            for(Order order: cookingDelayed){
                System.out.println(order.getId());
            }

            //delivery delayed
            LocalDateTime deliveryThreshold = now.minusMinutes(40);
            List<Order> deliveryDelayed = orderRepository.findDeliveryDelayed(deliveryThreshold);

            for(Order order: deliveryDelayed){
                System.out.println("delayed order id ==> " + order.getId());
                long order_id = order.getId();
                String message = "your order is delayed please be patient we will reach to you asap...";
                NotificationTypes type = NotificationTypes.DELIVERY_DELAYED;
                notificationService.sendNotification(order_id, message, type);
            }

        } catch (RuntimeException e) {
            System.out.println("=== delayed order scheduler error ===" + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("scheduled service failed due to an unexpected error.");
        }
    }
}
