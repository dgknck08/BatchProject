package com.example.springProject.dto;

public class StatisticsSummaryDto {
    private Long totalUsers;
    private Long totalActiveIssues;
    private Long totalOverdueIssues;
    private Long totalCompletedIssues;

    public StatisticsSummaryDto() {}

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalActiveIssues() { return totalActiveIssues; }
    public void setTotalActiveIssues(Long totalActiveIssues) { this.totalActiveIssues = totalActiveIssues; }

    public Long getTotalOverdueIssues() { return totalOverdueIssues; }
    public void setTotalOverdueIssues(Long totalOverdueIssues) { this.totalOverdueIssues = totalOverdueIssues; }

    public Long getTotalCompletedIssues() { return totalCompletedIssues; }
    public void setTotalCompletedIssues(Long totalCompletedIssues) { this.totalCompletedIssues = totalCompletedIssues; }
}