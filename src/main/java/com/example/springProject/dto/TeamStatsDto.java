package com.example.springProject.dto;

import java.util.List;

public class TeamStatsDto {
    private String teamName;
    private Long totalMembers;
    private Long totalActiveIssues;
    private Long totalCompletedIssues;
    private Double teamCompletionRate;
    private List<UserStatisticsDto> topPerformers;
    private List<UserStatisticsDto> membersWithOverdueIssues;

    public TeamStatsDto() {}

    // Getters and Setters
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Long getTotalMembers() { return totalMembers; }
    public void setTotalMembers(Long totalMembers) { this.totalMembers = totalMembers; }

    public Long getTotalActiveIssues() { return totalActiveIssues; }
    public void setTotalActiveIssues(Long totalActiveIssues) { this.totalActiveIssues = totalActiveIssues; }

    public Long getTotalCompletedIssues() { return totalCompletedIssues; }
    public void setTotalCompletedIssues(Long totalCompletedIssues) { this.totalCompletedIssues = totalCompletedIssues; }

    public Double getTeamCompletionRate() { return teamCompletionRate; }
    public void setTeamCompletionRate(Double teamCompletionRate) { this.teamCompletionRate = teamCompletionRate; }

    public List<UserStatisticsDto> getTopPerformers() { return topPerformers; }
    public void setTopPerformers(List<UserStatisticsDto> topPerformers) { this.topPerformers = topPerformers; }

    public List<UserStatisticsDto> getMembersWithOverdueIssues() { return membersWithOverdueIssues; }
    public void setMembersWithOverdueIssues(List<UserStatisticsDto> membersWithOverdueIssues) { 
        this.membersWithOverdueIssues = membersWithOverdueIssues; 
    }
}