package com.example.springProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RedmineResponse {
    
    @JsonProperty("issues")
    private List<RedmineIssue> issues;
    
    @JsonProperty("projects")
    private List<RedmineProject> projects;
    
    @JsonProperty("users")
    private List<RedmineUser> users;
    
    @JsonProperty("trackers")
    private List<RedmineTracker> trackers;
    
    @JsonProperty("issue_statuses")
    private List<RedmineStatus> statuses;
    
    @JsonProperty("issue_priorities")
    private List<RedminePriority> priorities;

    // Getters and setters
    public List<RedmineIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<RedmineIssue> issues) {
        this.issues = issues;
    }

    public List<RedmineProject> getProjects() {
        return projects;
    }

    public void setProjects(List<RedmineProject> projects) {
        this.projects = projects;
    }

    public List<RedmineUser> getUsers() {
        return users;
    }

    public void setUsers(List<RedmineUser> users) {
        this.users = users;
    }

    public List<RedmineTracker> getTrackers() {
        return trackers;
    }

    public void setTrackers(List<RedmineTracker> trackers) {
        this.trackers = trackers;
    }

    public List<RedmineStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<RedmineStatus> statuses) {
        this.statuses = statuses;
    }

    public List<RedminePriority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<RedminePriority> priorities) {
        this.priorities = priorities;
    }
}