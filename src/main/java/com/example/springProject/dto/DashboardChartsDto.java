package com.example.springProject.dto;

import java.util.List;

public class DashboardChartsDto {
    private List<UserStatisticsDto> userStats;
    private List<UserStatisticsDto> topPerformers;
    private List<UserStatisticsDto> usersWithOverdue;

    public DashboardChartsDto() {}

    // Getters and Setters
    public List<UserStatisticsDto> getUserStats() { return userStats; }
    public void setUserStats(List<UserStatisticsDto> userStats) { this.userStats = userStats; }

    public List<UserStatisticsDto> getTopPerformers() { return topPerformers; }
    public void setTopPerformers(List<UserStatisticsDto> topPerformers) { this.topPerformers = topPerformers; }

    public List<UserStatisticsDto> getUsersWithOverdue() { return usersWithOverdue; }
    public void setUsersWithOverdue(List<UserStatisticsDto> usersWithOverdue) { this.usersWithOverdue = usersWithOverdue; }
}