package com.example.springProject.dto;

public class ProjectStatsDto {
    private String projectName;
    private String projectIdentifier;
    private Long totalIssues;
    private Long completedIssues;
    private Double completionRate;

    public ProjectStatsDto() {}

    public ProjectStatsDto(String projectName, String projectIdentifier, Long totalIssues, Long completedIssues) {
        this.projectName = projectName;
        this.projectIdentifier = projectIdentifier;
        this.totalIssues = totalIssues;
        this.completedIssues = completedIssues;
        this.completionRate = totalIssues > 0 ? (double) completedIssues / totalIssues * 100 : 0.0;
    }

    // Getters and Setters
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getProjectIdentifier() { return projectIdentifier; }
    public void setProjectIdentifier(String projectIdentifier) { this.projectIdentifier = projectIdentifier; }

    public Long getTotalIssues() { return totalIssues; }
    public void setTotalIssues(Long totalIssues) { this.totalIssues = totalIssues; }

    public Long getCompletedIssues() { return completedIssues; }
    public void setCompletedIssues(Long completedIssues) { this.completedIssues = completedIssues; }

    public Double getCompletionRate() { return completionRate; }
    public void setCompletionRate(Double completionRate) { this.completionRate = completionRate; }
}