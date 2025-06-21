package com.example.springProject.service.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.example.springProject.exception.ApiException;
import com.example.springProject.model.RedmineResponse;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RedmineService {

    private static final Logger logger = LoggerFactory.getLogger(RedmineService.class);
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${redmine.api.url}")
    private String redmineApiUrl;

    @Value("${redmine.api.key}")
    private String redmineApiKey;

    @Value("${redmine.api.timeout:30000}")
    private int apiTimeout;

    private String buildApiUrl(String endpoint, String queryParams) {
        return redmineApiUrl + endpoint + queryParams;
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Redmine-API-Key", redmineApiKey);
        return headers;
    }
    
    private RedmineResponse fetchFromRedmine(String url, String entityType) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            logger.debug("Fetching {} from URL: {}", entityType, url.replaceAll("key=[^&]*", "key=***"));
            
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<RedmineResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, entity, RedmineResponse.class);
            
            RedmineResponse response = responseEntity.getBody();
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            
            if (response != null) {
                logger.info("{} successfully fetched from Redmine in {} ms", entityType, duration.toMillis());
            } else {
                logger.warn("Empty response received for {} from Redmine", entityType);
            }
            
            return response;
            
        } catch (HttpClientErrorException e) {
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            String errorMsg = String.format("Client error fetching %s from Redmine. Status: %s, Response: %s, Duration: %d ms", 
                                           entityType, e.getStatusCode(), e.getResponseBodyAsString(), duration.toMillis());
            logger.error(errorMsg);
            throw new ApiException(errorMsg, e.getStatusCode().value(), url);
            
        } catch (HttpServerErrorException e) {
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            String errorMsg = String.format("Server error fetching %s from Redmine. Status: %s, Response: %s, Duration: %d ms", 
                                           entityType, e.getStatusCode(), e.getResponseBodyAsString(), duration.toMillis());
            logger.error(errorMsg);
            throw new ApiException(errorMsg, e.getStatusCode().value(), url);
            
        } catch (ResourceAccessException e) {
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            String errorMsg = String.format("Connection timeout or network error fetching %s from Redmine. Duration: %d ms", 
                                           entityType, duration.toMillis());
            logger.error(errorMsg);
            throw new ApiException(errorMsg, 503, url);
            
        } catch (Exception e) {
            Duration duration = Duration.between(startTime, LocalDateTime.now());
            String errorMsg = String.format("Unexpected error fetching %s from Redmine: %s, Duration: %d ms", 
                                           entityType, e.getMessage(), duration.toMillis());
            logger.error(errorMsg, e);
            throw new ApiException(errorMsg, 500, url);
        }
    }
    //tüm issueları (closed,open olarak çekmek için).
    public RedmineResponse fetchAllIssuesFromRedmine(int offset, int limit) {
        String url = buildApiUrl("/issues.json", "?status_id=*&limit=" + limit + "&offset=" + offset);
        return fetchFromRedmine(url, "All Issues");
    }

    // Diğer metodlar sabit kaldı
    public RedmineResponse fetchTrackersFromRedmine() {
        String url = buildApiUrl("/trackers.json", "?limit=100");
        return fetchFromRedmine(url, "Trackers");
    }

    public RedmineResponse fetchUsersFromRedmine() {
        String url = buildApiUrl("/users.json", "?limit=100");
        return fetchFromRedmine(url, "Users");
    }

    public RedmineResponse fetchProjectsFromRedmine() {
        String url = buildApiUrl("/projects.json", "?limit=100");
        return fetchFromRedmine(url, "Projects");
    }
}
