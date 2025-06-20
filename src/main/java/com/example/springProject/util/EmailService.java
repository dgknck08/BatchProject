package com.example.springProject.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${notification.email.enabled:false}")
    private boolean emailNotificationEnabled;
    
    @Value("${notification.email.to}")
    private String[] toEmails;
    
    @Value("${notification.email.from}")
    private String fromEmail;
    
    @Value("${spring.application.name:Redmine Batch Service}")
    private String applicationName;

    public void sendSuccessEmail(String processType, Duration duration) {
        if (!emailNotificationEnabled) {
            return;
        }
        
        try {
            String subject = String.format("[%s] %s - SUCCESS", applicationName, processType);
            String body = buildSuccessEmailBody(processType, duration);
            
            sendEmail(subject, body);
            logger.info("Success notification email sent for {}", processType);
            
        } catch (Exception e) {
            logger.error("Failed to send success email notification: {}", e.getMessage());
        }
    }

    public void sendErrorEmail(String processType, String errorMessage, Duration duration) {
        if (!emailNotificationEnabled) {
            return;
        }
        
        try {
            String subject = String.format("[%s] %s - ERROR", applicationName, processType);
            String body = buildErrorEmailBody(processType, errorMessage, duration);
            
            sendEmail(subject, body);
            logger.info("Error notification email sent for {}", processType);
            
        } catch (Exception e) {
            logger.error("Failed to send error email notification: {}", e.getMessage());
        }
    }

    private void sendEmail(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmails);
        message.setSubject(subject);
        message.setText(body);
        
        mailSender.send(message);
    }

    private String buildSuccessEmailBody(String processType, Duration duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return String.format(
            "Redmine Batch Process Completed Successfully\n\n" +
            "Process Type: %s\n" +
            "Completion Time: %s\n" +
            "Duration: %d seconds\n" +
            "Status: SUCCESS\n\n" +
            "All data has been synchronized successfully with Redmine.\n\n" +
            "Best regards,\n" +
            "%s",
            processType,
            LocalDateTime.now().format(formatter),
            duration.getSeconds(),
            applicationName
        );
    }

    private String buildErrorEmailBody(String processType, String errorMessage, Duration duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return String.format(
            "Redmine Batch Process Failed\n\n" +
            "Process Type: %s\n" +
            "Failure Time: %s\n" +
            "Duration: %d seconds\n" +
            "Status: ERROR\n\n" +
            "Error Details:\n" +
            "%s\n\n" +
            "Please check the application logs for more detailed information.\n\n" +
            "Best regards,\n" +
            "%s",
            processType,
            LocalDateTime.now().format(formatter),
            duration.getSeconds(),
            errorMessage,
            applicationName
        );
    }
}