package com.example.springProject.util;

import com.example.springProject.model.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RedmineIssueDeserializer extends JsonDeserializer<RedmineIssue> {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public RedmineIssue deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        RedmineIssue issue = new RedmineIssue();

        // Basic fields
        issue.setId(node.get("id").asInt());
        issue.setSubject(getStringValue(node, "subject"));
        issue.setDescription(getStringValue(node, "description"));
        issue.setDoneRatio(node.has("done_ratio") ? node.get("done_ratio").asInt() : 0);
        issue.setPrivate(node.has("is_private") && node.get("is_private").asBoolean());
        issue.setSpentHours(node.has("spent_hours") ? node.get("spent_hours").asInt() : 0);

        // Handle estimated_hours (can be null)
        if (node.has("estimated_hours") && !node.get("estimated_hours").isNull()) {
            issue.setEstimatedHours(node.get("estimated_hours").asInt());
        }

        // Date fields
        if (node.has("start_date") && !node.get("start_date").isNull()) {
            issue.setStartDate(LocalDate.parse(node.get("start_date").asText(), DATE_FORMATTER));
        }
        
        if (node.has("due_date") && !node.get("due_date").isNull()) {
            issue.setDueDate(LocalDate.parse(node.get("due_date").asText(), DATE_FORMATTER));
        }

        if (node.has("created_on") && !node.get("created_on").isNull()) {
            issue.setCreatedOn(LocalDateTime.parse(node.get("created_on").asText(), DATETIME_FORMATTER));
        }

        if (node.has("updated_on") && !node.get("updated_on").isNull()) {
            issue.setUpdatedOn(LocalDateTime.parse(node.get("updated_on").asText(), DATETIME_FORMATTER));
        }

        if (node.has("closed_on") && !node.get("closed_on").isNull()) {
            issue.setClosedOn(LocalDateTime.parse(node.get("closed_on").asText(), DATETIME_FORMATTER));
        }

        // Nested obje - Project
        if (node.has("project") && !node.get("project").isNull()) {
            JsonNode projectNode = node.get("project");
            RedmineProject project = new RedmineProject();
            project.setId(projectNode.get("id").asInt());
            project.setName(getStringValue(projectNode, "name"));
            issue.setProject(project);
        }

        // Nested obje - Tracker
        if (node.has("tracker") && !node.get("tracker").isNull()) {
            JsonNode trackerNode = node.get("tracker");
            RedmineTracker tracker = new RedmineTracker();
            tracker.setId(trackerNode.get("id").asInt());
            tracker.setName(getStringValue(trackerNode, "name"));
            issue.setTracker(tracker);
        }

        // Nested obje - Status
        if (node.has("status") && !node.get("status").isNull()) {
            JsonNode statusNode = node.get("status");
            RedmineStatus status = new RedmineStatus();
            status.setId(statusNode.get("id").asInt());
            status.setName(getStringValue(statusNode, "name"));
            if (statusNode.has("is_closed")) {
                status.setClosed(statusNode.get("is_closed").asBoolean());
            }
            issue.setStatus(status);
        }

        // Nested obje - priority
        if (node.has("priority") && !node.get("priority").isNull()) {
            JsonNode priorityNode = node.get("priority");
            RedminePriority priority = new RedminePriority();
            priority.setId(priorityNode.get("id").asInt());
            priority.setName(getStringValue(priorityNode, "name"));
            issue.setPriority(priority);
        }

        // Nested obje - author
        if (node.has("author") && !node.get("author").isNull()) {
            JsonNode authorNode = node.get("author");
            RedmineUser author = new RedmineUser();
            author.setId(authorNode.get("id").asInt());
            
            //Firstname Lastname
            if (authorNode.has("name")) {
                String fullName = authorNode.get("name").asText();
                String[] nameParts = fullName.split(" ", 2);
                author.setFirstname(nameParts.length > 0 ? nameParts[0] : "");
                author.setLastname(nameParts.length > 1 ? nameParts[1] : "");
            }
            
            // Set default login if not provided
            author.setLogin("author_" + author.getId());
            issue.setAuthor(author);
        }

        // Nested obje - Assigned To
        if (node.has("assigned_to") && !node.get("assigned_to").isNull()) {
            JsonNode assignedNode = node.get("assigned_to");
            RedmineUser assignedUser = new RedmineUser();
            assignedUser.setId(assignedNode.get("id").asInt());
            
            // Handle name field
            if (assignedNode.has("name")) {
                String fullName = assignedNode.get("name").asText();
                String[] nameParts = fullName.split(" ", 2);
                assignedUser.setFirstname(nameParts.length > 0 ? nameParts[0] : "");
                assignedUser.setLastname(nameParts.length > 1 ? nameParts[1] : "");
            }
            
            // Set default login 
            assignedUser.setLogin("user_" + assignedUser.getId());
            issue.setAssignedTo(assignedUser);
        }

        return issue;
    }

    private String getStringValue(JsonNode node, String fieldName) {
        if (node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return "";
    }
}
