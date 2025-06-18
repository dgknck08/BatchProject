package com.example.springProject.controller;

import com.example.springProject.dto.UserStatisticsDto;
import com.example.springProject.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/user/{username}")
    public UserStatisticsDto getStats(@PathVariable String username) {
        return statisticsService.getUserStats(username);
    }
}
