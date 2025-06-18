package com.example.springProject.dto;

public class PriorityStatsDto {
    private int priorityId;
    private String priorityName;
    private Long issueCount;

    public PriorityStatsDto() {}

    public PriorityStatsDto(int priorityId, String priorityName, Long issueCount) {
        this.priorityId = priorityId;
        this.priorityName = priorityName;
        this.issueCount = issueCount;
    }

    // Getters and Setters
    public int getPriorityId() { return priorityId; }
    public void setPriorityId(int priorityId) { this.priorityId = priorityId; }

    public String getPriorityName() { return priorityName; }
    public void setPriorityName(String priorityName) { this.priorityName = priorityName; }

    public Long getIssueCount() { return issueCount; }
    public void setIssueCount(Long issueCount) { this.issueCount = issueCount; }
}