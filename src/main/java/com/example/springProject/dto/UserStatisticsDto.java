package com.example.springProject.dto;

import java.util.List;

public class UserStatisticsDto {
    private String username;
    private String firstName;
    private String lastName;
    private Long completedIssueCount;
    private Long totalIssueCount;
    private Long activeIssueCount;
    private Long overdueIssueCount;
    private Long closedIssueCount;
    
    private Double completionRate;
    private Long totalEstimatedHours;
    private Long totalSpentHours;
    private List<ProjectStatsDto> projectStats;
    private List<StatusStatsDto> statusStats;
    private List<PriorityStatsDto> priorityStats;

    public UserStatisticsDto() {}

    public UserStatisticsDto(String username, Long completedIssueCount, Long totalIssueCount) {
        this.username = username;
        this.completedIssueCount = completedIssueCount;
        this.totalIssueCount = totalIssueCount;
        this.completionRate = totalIssueCount > 0 ? (double) completedIssueCount / totalIssueCount * 100 : 0.0;
    }
    
    public Long getClosedIssueCount() {
		return closedIssueCount;
	}

	public void setClosedIssueCount(Long closedIssueCount) {
		this.closedIssueCount = closedIssueCount;
	}

	public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Long getCompletedIssueCount() { return completedIssueCount; }
    public void setCompletedIssueCount(Long completedIssueCount) { 
        this.completedIssueCount = completedIssueCount;
        calculateCompletionRate();
    }

    public Long getTotalIssueCount() { return totalIssueCount; }
    public void setTotalIssueCount(Long totalIssueCount) { 
        this.totalIssueCount = totalIssueCount;
        calculateCompletionRate();
    }

    public Long getActiveIssueCount() { return activeIssueCount; }
    public void setActiveIssueCount(Long activeIssueCount) { this.activeIssueCount = activeIssueCount; }

    public Long getOverdueIssueCount() { return overdueIssueCount; }
    public void setOverdueIssueCount(Long overdueIssueCount) { this.overdueIssueCount = overdueIssueCount; }

    public Double getCompletionRate() { return completionRate; }
    public void setCompletionRate(Double completionRate) { this.completionRate = completionRate; }

    public Long getTotalEstimatedHours() { return totalEstimatedHours; }
    public void setTotalEstimatedHours(Long totalEstimatedHours) { this.totalEstimatedHours = totalEstimatedHours; }

    public Long getTotalSpentHours() { return totalSpentHours; }
    public void setTotalSpentHours(Long totalSpentHours) { this.totalSpentHours = totalSpentHours; }

    public List<ProjectStatsDto> getProjectStats() { return projectStats; }
    public void setProjectStats(List<ProjectStatsDto> projectStats) { this.projectStats = projectStats; }

    public List<StatusStatsDto> getStatusStats() { return statusStats; }
    public void setStatusStats(List<StatusStatsDto> statusStats) { this.statusStats = statusStats; }

    public List<PriorityStatsDto> getPriorityStats() { return priorityStats; }
    public void setPriorityStats(List<PriorityStatsDto> priorityStats) { this.priorityStats = priorityStats; }

    private void calculateCompletionRate() {
        if (totalIssueCount != null && totalIssueCount > 0 && completedIssueCount != null) {
            this.completionRate = (double) completedIssueCount / totalIssueCount * 100;
        } else {
            this.completionRate = 0.0;
        }
    }
}