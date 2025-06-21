package com.example.springProject.service.batch;

import com.example.springProject.model.*;
import com.example.springProject.util.EmailService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class RedmineProccesorService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RedmineProccesorService.class);

    private final RedmineService redmineService;
    private final DataPersistenceService dataPersistenceService;
    private final EmailService emailService;

    @Autowired
    public RedmineProccesorService(RedmineService redmineService,
                                   DataPersistenceService dataPersistenceService,
                                   EmailService emailService) {
        this.redmineService = redmineService;
        this.dataPersistenceService = dataPersistenceService;
        this.emailService = emailService;
    }

    public void processAll() {
        processUsers();
        processProjects();
        processTrackers();
        processAllIssues();
    }

    public <T> void processEntity(Supplier<RedmineResponse> fetchFunction,
                                  Function<RedmineResponse, List<T>> extractData,
                                  Consumer<List<T>> saveFunction,
                                  Consumer<List<T>> deleteFunction,
                                  String entityName) {
        logger.info("⏳ Starting processing for: {}", entityName);
        Instant start = Instant.now();

        try {
            RedmineResponse response = fetchFunction.get();
            List<T> data = extractData.apply(response);

            if (data == null || data.isEmpty()) {
                logger.warn("No {} data found", entityName);
                return;
            }

            saveFunction.accept(data);
            deleteFunction.accept(data);

            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            logger.info("Finished processing {} in {}ms", entityName, duration.toMillis());
        } catch (Exception e) {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);

            String errorMessage = String.format("Error processing %s: %s", entityName, e.getMessage());
            logger.error(errorMessage, e);

            emailService.sendErrorEmail(entityName, e.getMessage(), duration);
        }
    }

    public void processUsers() {
        processEntity(
                redmineService::fetchUsersFromRedmine,
                RedmineResponse::getUsers,
                dataPersistenceService::saveUsers,
                dataPersistenceService::deleteNonMatchingUsers,
                "Users"
        );
    }

    public void processProjects() {
        processEntity(
                redmineService::fetchProjectsFromRedmine,
                RedmineResponse::getProjects,
                dataPersistenceService::saveProjects,
                dataPersistenceService::deleteNonMatchingProjects,
                "Projects"
        );
    }

    public void processTrackers() {
        processEntity(
                redmineService::fetchTrackersFromRedmine,
                RedmineResponse::getTrackers,
                dataPersistenceService::saveTrackers,
                dataPersistenceService::deleteNonMatchingTrackers,
                "Trackers"
        );
    }

    public void processAllIssues() {
        int offset = 0;
        int limit = 100;

        while (true) {
            RedmineResponse response = redmineService.fetchAllIssuesFromRedmine(offset, limit);
            List<RedmineIssue> issues = response.getIssues();

            if (issues == null || issues.isEmpty()) break;

            dataPersistenceService.saveIssues(issues);  
            offset += limit;
        }
    }

}
