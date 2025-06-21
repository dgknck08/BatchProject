package com.example.springProject.service.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.springProject.exception.ApiException;
import com.example.springProject.exception.DatabaseException;
import com.example.springProject.util.EmailService;

import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class RedmineBatchService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RedmineBatchService.class);

    private final RedmineProccesorService redmineProcessorService;
    private final EmailService emailService;

    @Autowired
    public RedmineBatchService(RedmineProccesorService redmineProcessorService, EmailService emailService) {
        this.redmineProcessorService = redmineProcessorService;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        logger.info("Redmine Batch Application started at {}", LocalDateTime.now());
        executeBatchProcess("Initial data loading");
    }

    @Scheduled(cron = "${batch.cron.expression}") // application.properties
    public void runScheduledBatchProcess() {
        logger.info("Scheduled batch process started at {}", LocalDateTime.now());
        executeBatchProcess("Scheduled batch process");
    }

    private void executeBatchProcess(String processType) {
        LocalDateTime startTime = LocalDateTime.now();
        boolean success = false;
        String errorMessage = null;

        try {
            logger.info("🚀 Starting {}", processType);

            // İşlem sırası önemli - dependencies göz önünde bulundurularak:
            // 1. Projects (diğer entityler için referans)
            // 2. Users (Issues için assigneto referansı)
            // 3. Trackers (Issues için tracker referansı)
            // 4. Issues (en son, diğer entitylere bağımlı)
            
            redmineProcessorService.processProjects();
            redmineProcessorService.processUsers();
            redmineProcessorService.processTrackers();
            redmineProcessorService.processAllIssues();
           

            success = true;
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            logger.info("{} completed successfully in {} seconds", processType, duration.getSeconds());

        } catch (ApiException e) {
            errorMessage = String.format("API Error - Status: %d, Message: %s, Endpoint: %s",
                e.getStatusCode(), e.getMessage(), e.getEndpoint());
            handleException(errorMessage, e);
        } catch (DatabaseException e) {
            errorMessage = String.format("Database Error - Query: %s, Message: %s",
                e.getFailedQuery(), e.getMessage());
            handleException(errorMessage, e);
        } catch (Exception e) {
            errorMessage = "Unexpected error: " + e.getMessage();
            handleException(errorMessage, e);
        } finally {
            Duration totalDuration = Duration.between(startTime, LocalDateTime.now());

            try {
                if (success) {
                    emailService.sendSuccessEmail(processType, totalDuration);
                } else {
                    emailService.sendErrorEmail(processType, errorMessage, totalDuration);
                }
            } catch (Exception emailException) {
                // Email gönderiminde hata olsa bile batch işlemi devam etsin istiyorum.
                logger.error("Failed to send notification email: {}", emailException.getMessage(), emailException);
            }

            logger.info("{} finished. Total duration: {} seconds", processType, totalDuration.getSeconds());
        }
    }

    private void handleException(String message, Exception e) {
        logger.error("{}", message, e);
    }

    // Manuel tetikleme için ek metodlar 
    public void runManualProcess() {
        logger.info("Manual batch process triggered at {}", LocalDateTime.now());
        executeBatchProcess("Manual batch process");
    }

    // Sadece belirli entityleri işlemek için metodlar
    public void processOnlyUsers() {
        logger.info("Processing only Users at {}", LocalDateTime.now());
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            redmineProcessorService.processUsers();
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            logger.info("Users processing completed in {} seconds", duration.getSeconds());
        } catch (Exception e) {
            logger.error("Error processing Users: {}", e.getMessage(), e);
        }
    }

    public void processOnlyProjects() {
        logger.info("Processing only Projects at {}", LocalDateTime.now());
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            redmineProcessorService.processProjects();
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            logger.info("Projects processing completed in {} seconds", duration.getSeconds());
        } catch (Exception e) {
            logger.error("Error processing Projects: {}", e.getMessage(), e);
        }
    }
}