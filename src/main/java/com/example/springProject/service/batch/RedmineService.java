package com.example.springProject.service.batch;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.example.springProject.exception.ApiException; 
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.example.springProject.model.RedmineResponse;



@Service
public class RedmineService {

    private static final Logger logger =  LoggerFactory.getLogger(RedmineService.class);
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${redmine.api.url}")
    private String redmineApiUrl;

    @Value("${redmine.api.key}")
    private String redmineApiKey;

    @Value("${cron.job.expression}")
    private String cronExpression;  
    
    private String buildApiUrl(String endpoint) {
        return redmineApiUrl + endpoint + "?key=" + redmineApiKey;
    }
    
    public RedmineResponse fetchIssuesFromRedmine() {
        String url = buildApiUrl("/issues.json");
        return fetchFromRedmine(url, "issue");
    }

    public RedmineResponse fetchTrackersFromRedmine() {
        String url = buildApiUrl("/trackers.json");
        return fetchFromRedmine(url, "tracker");
    }

    public RedmineResponse fetchUsersFromRedmine() {
        String url = buildApiUrl("/users.json");
        return fetchFromRedmine(url, "user");
    }

    public RedmineResponse fetchProjectsFromRedmine() {
        String url = buildApiUrl("/projects.json");
        return fetchFromRedmine(url, "project");
    }

    private RedmineResponse fetchFromRedmine(String url, String entityType) {
        try {
            RedmineResponse response = restTemplate.getForObject(url, RedmineResponse.class);
            logRedmineFetchResult(response, entityType);
            return response;
        } catch (Exception e) {
            logger.error("Error fetching {} from Redmine: ", entityType, e);
            throw new ApiException("Failed to fetch " + entityType + " from Redmine.", 500, url);
        }
    }

    private void logRedmineFetchResult(RedmineResponse response, String entityType) {
        if (response != null) {
            logger.info("{} successfully retrieved from Redmine.", entityType);
        } else {
            logger.warn("No {} found in Redmine.", entityType);
        }
    }
}



   
