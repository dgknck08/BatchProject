package com.example.springProject.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springProject.exception.ApiException;
import com.example.springProject.exception.DatabaseException;
import com.example.springProject.model.RedmineResponse;


import ch.qos.logback.classic.Logger;

@Service
public class RedmineProccesorService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RedmineProccesorService.class);

    private final DataPersistenceService dataPersistenceService;
    private final RedmineService redmineService;

    @Autowired
    public RedmineProccesorService(DataPersistenceService dataPersistenceService, RedmineService redmineService) {
        this.dataPersistenceService = dataPersistenceService;
        this.redmineService = redmineService;
    }

    private <T> void process(
            Supplier<RedmineResponse> fetchFunction,
            Function<RedmineResponse, List<T>> getDataFunction,
            Consumer<List<T>> saveFunction,
            Consumer<List<T>> deleteFunction,
            String logEntityName
    ) {
        try {
            RedmineResponse response = fetchFunction.get();

            if (response != null) {
                List<T> entities = getDataFunction.apply(response);

                if (entities != null && !entities.isEmpty()) {
                    deleteFunction.accept(entities);
                    saveFunction.accept(entities);
                    logger.info("{} data has been successfully saved.", logEntityName);
                } else {
                    logger.warn("{} data is not available.", logEntityName);
                }
            } else {
                throw new ApiException("No response received from the API.", 500, logEntityName);
            }
        } catch (ApiException e) {
            handleException("An error occurred during the API call", e);
        } catch (DatabaseException e) {
            handleException("An error occurred during database operations", e);
        } catch (Exception e) {
            handleException("An unknown error occurred", e);
        }
    }

    private void handleException(String message, Exception e) {
        logger.error("{}: {} | Status Code: {} | Error Details: {}", 
                     message, e.getMessage(), e instanceof ApiException ? ((ApiException) e).getStatusCode() : "N/A", e);
    }

    public void processTrackers() {
        process(
            redmineService::fetchTrackersFromRedmine,
            RedmineResponse::getTrackers,
            dataPersistenceService::saveTrackers,
            dataPersistenceService::deleteNonMatchingTrackers,
            "Trackers"
        );
    }

    public void processUsers() {
        process(
            redmineService::fetchUsersFromRedmine,
            RedmineResponse::getUsers,
            dataPersistenceService::saveUsers,
            dataPersistenceService::deleteNonMatchingUsers,
            "Users"
        );
    }

    public void processProjects() {
        process(
            redmineService::fetchProjectsFromRedmine,
            RedmineResponse::getProjects,
            dataPersistenceService::saveProjects,
            dataPersistenceService::deleteNonMatchingProjects,
            "Projects"
        );
    }

    public void processIssues() {
        process(
            redmineService::fetchIssuesFromRedmine,
            RedmineResponse::getIssues,
            dataPersistenceService::saveIssues,
            dataPersistenceService::deleteNonMatchingIssues,
            "Issues"
        );
    }
}
