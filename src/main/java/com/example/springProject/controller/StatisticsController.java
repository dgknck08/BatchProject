package com.example.springProject.controller;

import com.example.springProject.dto.*;
import com.example.springProject.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserStatisticsDto> getUserStats(@PathVariable String username) {
        UserStatisticsDto stats = statisticsService.getUserStats(username);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserStatisticsDto>> getAllUsersStats() {
        List<UserStatisticsDto> allStats = statisticsService.getAllUsersStats();
        return ResponseEntity.ok(allStats);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto dashboardStats = statisticsService.getDashboardStats();
        return ResponseEntity.ok(dashboardStats);
    }

    @GetMapping("/user/{username}/trend")
    public ResponseEntity<List<UserPerformanceTrendDto>> getUserPerformanceTrend(
            @PathVariable String username,
            @RequestParam(defaultValue = "12") int weeks) {
        List<UserPerformanceTrendDto> trends = statisticsService.getUserPerformanceTrend(username, weeks);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/summary")
    public ResponseEntity<StatisticsSummaryDto> getStatisticsSummary() {
        List<UserStatisticsDto> allStats = statisticsService.getAllUsersStats();
        
        StatisticsSummaryDto summary = new StatisticsSummaryDto();
        summary.setTotalUsers((long) allStats.size());
        
        Long totalActive = allStats.stream()
                .mapToLong(s -> s.getActiveIssueCount() != null ? s.getActiveIssueCount() : 0)
                .sum();
        summary.setTotalActiveIssues(totalActive);
        
        Long totalOverdue = allStats.stream()
                .mapToLong(s -> s.getOverdueIssueCount() != null ? s.getOverdueIssueCount() : 0)
                .sum();
        summary.setTotalOverdueIssues(totalOverdue);
        
        Long totalCompleted = allStats.stream()
                .mapToLong(s -> s.getCompletedIssueCount() != null ? s.getCompletedIssueCount() : 0)
                .sum();
        summary.setTotalCompletedIssues(totalCompleted);
        
        return ResponseEntity.ok(summary);
    }
}
