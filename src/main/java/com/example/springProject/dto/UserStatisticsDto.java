package com.example.springProject.dto;

public class UserStatisticsDto {
    private String username;
    private Long completedIssueCount;
    private Long totalIssueCount;

    public UserStatisticsDto(String username, Long completedIssueCount, Long totalIssueCount) {
        this.username = username;
        this.completedIssueCount = completedIssueCount;
        this.totalIssueCount = totalIssueCount;
    }

    public String getUsername() {
        return username;
    }

    public Long getCompletedIssueCount() {
        return completedIssueCount;
    }

    public Long getTotalIssueCount() {
        return totalIssueCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCompletedIssueCount(Long completedIssueCount) {
        this.completedIssueCount = completedIssueCount;
    }

    public void setTotalIssueCount(Long totalIssueCount) {
        this.totalIssueCount = totalIssueCount;
    }
}
