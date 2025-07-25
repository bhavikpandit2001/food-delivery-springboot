package com.assesment.food_delivery.utils;

import com.assesment.food_delivery.dto.EmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailRequestDto request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("delivery@yopmail.com"); // use the email configured
        message.setTo(request.getEmail());
        message.setSubject(request.getTypes().toString());
        message.setText(request.getMessage());
        mailSender.send(message);
        System.out.println("[EMAIL SENT] To: " + request.getEmail() + " | Subject: " + request.getTypes());
    }
}
