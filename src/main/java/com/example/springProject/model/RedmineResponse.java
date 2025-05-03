package com.example.springProject.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RedmineResponse {

    @JsonProperty("issues")
    private List<RedmineIssue> issues;

    @JsonProperty("users")
    private List<RedmineUser> users;

    @JsonProperty("projects")
    private List<RedmineProject> projects;

    @JsonProperty("trackers")
    private List<RedmineTracker> trackers;

    public List<RedmineIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<RedmineIssue> issues) {
        this.issues = issues;
    }



    public List<RedmineUser> getUsers() {
        return users;
    }

    public void setUsers(List<RedmineUser> users) {
        this.users = users;
    }

 

    public List<RedmineProject> getProjects() {
        return projects;
    }

    public void setProjects(List<RedmineProject> projects) {
        this.projects = projects;
    }

    public List<RedmineTracker> getTrackers() {
        return trackers;
    }

    public void setTrackers(List<RedmineTracker> trackers) {
        this.trackers = trackers;
    }
}
