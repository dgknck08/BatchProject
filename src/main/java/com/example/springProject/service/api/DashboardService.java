package com.example.springProject.service.api;

import com.example.springProject.dto.UserStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final StatisticsService statisticsService;

    @Autowired
    public DashboardService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public List<UserStatisticsDto> getTopPerformers(int limit) {
        return statisticsService.getAllUsersStats()
                .stream()
                .sorted((a, b) -> Double.compare(b.getCompletionRate(), a.getCompletionRate()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<UserStatisticsDto> getUsersWithOverdueIssues() {
        return statisticsService.getAllUsersStats()
                .stream()
                .filter(stats -> stats.getOverdueIssueCount() > 0)
                .sorted((a, b) -> Long.compare(b.getOverdueIssueCount(), a.getOverdueIssueCount()))
                .collect(Collectors.toList());
    }

    public List<UserStatisticsDto> getMostActiveUsers(int limit) {
        return statisticsService.getAllUsersStats()
                .stream()
                .sorted((a, b) -> Long.compare(b.getActiveIssueCount(), a.getActiveIssueCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}