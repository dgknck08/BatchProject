package com.example.springProject.util;

import org.slf4j.LoggerFactory; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

import org.springframework.mail.SimpleMailMessage;


@Service
public class EmailService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;
    
    @Value("${email.receiver}")
    private String toEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        try {
            mailSender.send(message);
            logger.info("Email successfully sent to {}.", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendSuccessEmail() {
        String subject = "Batch Application Started";
        String text = "Batch process completed successfully.";
        sendSimpleMessage(toEmail, subject, text);
    }

    public void sendErrorEmail(String errorMessage) {
        String subject = "Batch Application Started";
        String text = "An error occurred during the batch process: " + errorMessage;
        sendSimpleMessage(toEmail, subject, text);
    }
}