package com.example.springProject.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.example.springProject.util.RedmineIssueDeserializer;
import jakarta.persistence.*;

@Entity
@JsonDeserialize(using = RedmineIssueDeserializer.class)
@Table(name = "issues")
public class RedmineIssue {

    @Id
    @JsonProperty("id")
    private int id;

    @JsonProperty("project")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private RedmineProject project;

    @JsonProperty("assigned_to")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private RedmineUser assignedTo;

    @JsonProperty("tracker")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracker_id")
    private RedmineTracker tracker;

    @JsonProperty("status")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private RedmineStatus status;

    @JsonProperty("priority")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private RedminePriority priority;

    @JsonProperty("author")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private RedmineUser author;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @JsonProperty("done_ratio")
    private int doneRatio;

    @JsonProperty("is_private")
    private boolean isPrivate;

    @JsonProperty("estimated_hours")
    private Integer estimatedHours;

    @JsonProperty("spent_hours")
    private int spentHours;

    @JsonProperty("created_on")
    private LocalDateTime createdOn;

    @JsonProperty("updated_on")
    private LocalDateTime updatedOn;

    @JsonProperty("closed_on")
    private LocalDateTime closedOn;

    // --- Getters & Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RedmineProject getProject() {
        return project;
    }

    public void setProject(RedmineProject project) {
        this.project = project;
    }

    public RedmineUser getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(RedmineUser assignedTo) {
        this.assignedTo = assignedTo;
    }

    public RedmineTracker getTracker() {
        return tracker;
    }

    public void setTracker(RedmineTracker tracker) {
        this.tracker = tracker;
    }

    public RedmineStatus getStatus() {
        return status;
    }

    public void setStatus(RedmineStatus status) {
        this.status = status;
    }

    public RedminePriority getPriority() {
        return priority;
    }

    public void setPriority(RedminePriority priority) {
        this.priority = priority;
    }

    public RedmineUser getAuthor() {
        return author;
    }

    public void setAuthor(RedmineUser author) {
        this.author = author;
    }

    public String getSubject() {
        return subject != null ? subject : "";
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
    }

    public int getDoneRatio() {
        return doneRatio;
    }

    public void setDoneRatio(int doneRatio) {
        this.doneRatio = doneRatio;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getSpentHours() {
        return spentHours;
    }

    public void setSpentHours(int spentHours) {
        this.spentHours = spentHours;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public LocalDateTime getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(LocalDateTime closedOn) {
        this.closedOn = closedOn;
    }
}
