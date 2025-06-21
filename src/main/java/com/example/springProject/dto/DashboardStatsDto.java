package com.example.springProject.dto;

public class DashboardStatsDto {
    private Long totalUsers;
    private Long totalActiveIssues;
    private Long totalOverdueIssues;
    private Long totalCompletedIssues;
   
    private Long totalClosedIssues;
    private Long totalOpenIssues;
    private Long totalResolvedIssues;
    private Long totalAllIssues;
    
    private Double averageCompletionRate;
    private Long totalEstimatedHours;
    private Long totalSpentHours;

    public DashboardStatsDto() {}
    
    
    
    
    public Long getTotalClosedIssues() {
		return totalClosedIssues;
	}




	public void setTotalClosedIssues(Long totalClosedIssues) {
		this.totalClosedIssues = totalClosedIssues;
	}




	public Long getTotalOpenIssues() {
		return totalOpenIssues;
	}




	public void setTotalOpenIssues(Long totalOpenIssues) {
		this.totalOpenIssues = totalOpenIssues;
	}




	public Long getTotalResolvedIssues() {
		return totalResolvedIssues;
	}




	public void setTotalResolvedIssues(Long totalResolvedIssues) {
		this.totalResolvedIssues = totalResolvedIssues;
	}




	public Long getTotalAllIssues() {
		return totalAllIssues;
	}




	public void setTotalAllIssues(Long totalAllIssues) {
		this.totalAllIssues = totalAllIssues;
	}




	public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalActiveIssues() { return totalActiveIssues; }
    public void setTotalActiveIssues(Long totalActiveIssues) { this.totalActiveIssues = totalActiveIssues; }

    public Long getTotalOverdueIssues() { return totalOverdueIssues; }
    public void setTotalOverdueIssues(Long totalOverdueIssues) { this.totalOverdueIssues = totalOverdueIssues; }

    public Long getTotalCompletedIssues() { return totalCompletedIssues; }
    public void setTotalCompletedIssues(Long totalCompletedIssues) { this.totalCompletedIssues = totalCompletedIssues; }

    public Double getAverageCompletionRate() { return averageCompletionRate; }
    public void setAverageCompletionRate(Double averageCompletionRate) { this.averageCompletionRate = averageCompletionRate; }

    public Long getTotalEstimatedHours() { return totalEstimatedHours; }
    public void setTotalEstimatedHours(Long totalEstimatedHours) { this.totalEstimatedHours = totalEstimatedHours; }

    public Long getTotalSpentHours() { return totalSpentHours; }
    public void setTotalSpentHours(Long totalSpentHours) { this.totalSpentHours = totalSpentHours; }
}