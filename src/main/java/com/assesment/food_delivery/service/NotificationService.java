package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.EmailRequestDto;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.NotificationTypes;
import com.assesment.food_delivery.repository.UserRepository;
import com.assesment.food_delivery.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void sendNotification(Long order_id, String message, NotificationTypes type, Long user_id){
        System.out.println("=== mock notification sent ===");
        System.out.println("Order id => "+ order_id);
        System.out.println("Type => "+ type);
        System.out.println("Message => "+ message);
        System.out.println("customer id => "+ user_id);
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("user not found..."));

        if(user.getEmail() != null){
            EmailRequestDto email = new EmailRequestDto();
            email.setOrderId(order_id);
            email.setMessage(message);
            email.setTypes(type);
            email.setEmail(user.getEmail());
            emailService.sendEmail(email);
        }
        System.out.println("[NOTIFICATION] " + user.getUsername() + ": " + message);
    }
}
