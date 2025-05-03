package com.example.springProject.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.springProject.exception.ApiException;
import com.example.springProject.exception.DatabaseException;

import ch.qos.logback.classic.Logger;
@Service
public class RedmineBatchService implements CommandLineRunner {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RedmineBatchService.class);

    private final RedmineProccesorService redmineProccesorService;
    private final EmailService emailService;

    @Autowired
    public RedmineBatchService(RedmineProccesorService redmineProccesorService, EmailService emailService) {
        this.redmineProccesorService = redmineProccesorService;
        this.emailService = emailService;
    }

    private void processInitialData() {
        redmineProccesorService.processProjects();
        redmineProccesorService.processUsers();
        redmineProccesorService.processTrackers();
        redmineProccesorService.processIssues();
    }

    @Override
    public void run(String... args) {
        logger.info("Application started");
        executeBatchProcess("Initial data loading");
    }

    @Scheduled(cron = "${cron.job.expression}")
    public void runBatchProcess() {
        logger.info("...Currently in Batch...");
        executeBatchProcess("Scheduled Batch process");
    }

    private void executeBatchProcess(String processType) {
        try {
            processInitialData();
            logger.info("{} completed successfully.", processType);
            emailService.sendSuccessEmail();
        } catch (ApiException e) {
            handleException("An API error occurred: ", e);
        } catch (DatabaseException e) {
            handleException("A database error occurred: ", e);
        } catch (Exception e) {
            handleException("An unknown error occurred: ", e);
        }
    }

    private void handleException(String message, Exception e) {
        logger.error(message, e);
        emailService.sendErrorEmail(message + e.getMessage());
    }
}