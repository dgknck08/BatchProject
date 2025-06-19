package com.example.springProject.controller;

import com.example.springProject.dto.*;
import com.example.springProject.service.api.DashboardService;
import com.example.springProject.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final StatisticsService statisticsService;

    @Autowired
    public DashboardController(DashboardService dashboardService, StatisticsService statisticsService) {
        this.dashboardService = dashboardService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/top-performers")
    public ResponseEntity<List<UserStatisticsDto>> getTopPerformers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(dashboardService.getTopPerformers(limit));
    }

    @GetMapping("/overdue-issues")
    public ResponseEntity<List<UserStatisticsDto>> getUsersWithOverdueIssues() {
        return ResponseEntity.ok(dashboardService.getUsersWithOverdueIssues());
    }

    @GetMapping("/most-active")
    public ResponseEntity<List<UserStatisticsDto>> getMostActiveUsers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(dashboardService.getMostActiveUsers(limit));
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }

    @GetMapping("/charts-data")
    public ResponseEntity<DashboardChartsDto> getChartsData() {
        List<UserStatisticsDto> allStats = statisticsService.getAllUsersStats();
        
        DashboardChartsDto chartsData = new DashboardChartsDto();
        chartsData.setUserStats(allStats);
        chartsData.setTopPerformers(dashboardService.getTopPerformers(5));
        chartsData.setUsersWithOverdue(dashboardService.getUsersWithOverdueIssues());
        
        return ResponseEntity.ok(chartsData);
    }
}