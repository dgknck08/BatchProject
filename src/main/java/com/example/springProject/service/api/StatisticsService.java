package com.example.springProject.service.api;

import com.example.springProject.dto.UserStatisticsDto;
import com.example.springProject.repository.RedmineIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StatisticsService {

    private final RedmineIssueRepository issueRepository;

    @Autowired
    public StatisticsService(RedmineIssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public UserStatisticsDto getUserStats(String username) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusWeeks(1);

        Long completed = issueRepository.countCompletedIssuesInWeek(username, start, end);
        Long total = issueRepository.countTotalIssues(username, start, end);

        return new UserStatisticsDto(username, completed, total);
    }
}
