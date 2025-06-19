package com.example.springProject.dto;

import java.time.LocalDate;

public class UserPerformanceTrendDto {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private Long completedIssues;
    private Long totalIssues;
    private Double completionRate;

    public UserPerformanceTrendDto() {}

    public UserPerformanceTrendDto(LocalDate weekStart, LocalDate weekEnd, Long completedIssues, Long totalIssues) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.completedIssues = completedIssues;
        this.totalIssues = totalIssues;
        this.completionRate = totalIssues > 0 ? (double) completedIssues / totalIssues * 100 : 0.0;
    }

    // Getters and Setters
    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

    public LocalDate getWeekEnd() { return weekEnd; }
    public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }

    public Long getCompletedIssues() { return completedIssues; }
    public void setCompletedIssues(Long completedIssues) { 
        this.completedIssues = completedIssues;
        calculateCompletionRate();
    }

    public Long getTotalIssues() { return totalIssues; }
    public void setTotalIssues(Long totalIssues) { 
        this.totalIssues = totalIssues;
        calculateCompletionRate();
    }

    public Double getCompletionRate() { return completionRate; }
    public void setCompletionRate(Double completionRate) { this.completionRate = completionRate; }

    private void calculateCompletionRate() {
        if (totalIssues != null && totalIssues > 0 && completedIssues != null) {
            this.completionRate = (double) completedIssues / totalIssues * 100;
        } else {
            this.completionRate = 0.0;
        }
    }
}