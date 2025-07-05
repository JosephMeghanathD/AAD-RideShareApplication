package com.ride.share.aad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Spring Boot automatically configures this bean with the properties you set
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a simple plain text email.
     *
     * @param to      The recipient's email address.
     * @param subject The email subject.
     * @param text    The email body.
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        // The 'from' address can be set here or in application.properties (spring.mail.username)
        // message.setFrom("your-gmail-address@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // This line does the actual sending
        mailSender.send(message);
    }
}