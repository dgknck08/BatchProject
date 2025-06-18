package com.example.springProject.dto;


public class StatusStatsDto {
    private int statusId;
    private String statusName;
    private Long issueCount;

    public StatusStatsDto() {}

    public StatusStatsDto(int statusId, String statusName, Long issueCount) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.issueCount = issueCount;
    }

    // Getters and Setters
    public int getStatusId() { return statusId; }
    public void setStatusId(int statusId) { this.statusId = statusId; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }

    public Long getIssueCount() { return issueCount; }
    public void setIssueCount(Long issueCount) { this.issueCount = issueCount; }
}