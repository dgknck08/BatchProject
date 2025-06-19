package com.example.springProject.service.api;

import com.example.springProject.dto.*;
import com.example.springProject.model.RedmineIssue;
import com.example.springProject.model.RedmineUser;
import com.example.springProject.repository.RedmineIssueRepository;
import com.example.springProject.repository.RedmineUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final RedmineIssueRepository issueRepository;
    private final RedmineUserRepository userRepository;

    @Autowired
    public StatisticsService(RedmineIssueRepository issueRepository, RedmineUserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    public UserStatisticsDto getUserStats(String username) {
        Optional<RedmineUser> userOpt = userRepository.findByLogin(username);
        if (!userOpt.isPresent()) {
            return null;
        }

        RedmineUser user = userOpt.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.minusWeeks(1);
        LocalDateTime monthStart = now.minusMonths(1);

        UserStatisticsDto stats = new UserStatisticsDto();
        stats.setUsername(username);
        stats.setFirstName(user.getFirstname());
        stats.setLastName(user.getLastname());

        // Haftalık istatistikler
        Long weeklyCompleted = issueRepository.countCompletedIssuesInWeek(username, weekStart, now);
        Long weeklyTotal = issueRepository.countTotalIssues(username, weekStart, now);
        
        // Aylık istatistikler
        Long monthlyCompleted = issueRepository.countCompletedIssuesInWeek(username, monthStart, now);
        Long monthlyTotal = issueRepository.countTotalIssues(username, monthStart, now);

        // Genel istatistikler
        Long activeIssues = issueRepository.countIssuesByUsernameAndStatus(username, 1); // Aktif işler
        List<RedmineIssue> overdueIssues = issueRepository.findOverdueIssuesByUsername(username);
        Long overdueCount = (long) overdueIssues.size();

        stats.setCompletedIssueCount(weeklyCompleted);
        stats.setTotalIssueCount(weeklyTotal);
        stats.setActiveIssueCount(activeIssues);
        stats.setOverdueIssueCount(overdueCount);

        // Completion rate hesaplama
        if (weeklyTotal > 0) {
            stats.setCompletionRate((double) weeklyCompleted / weeklyTotal * 100);
        } else {
            stats.setCompletionRate(0.0);
        }

        // Kullanıcının tüm işleri
        List<RedmineIssue> userIssues = issueRepository.findAllByUsername(username);
        
        // Saat hesaplamaları
        calculateTimeStatistics(stats, userIssues);
        
        // Alt istatistikler
        stats.setProjectStats(getProjectStats(username, userIssues));
        stats.setStatusStats(getStatusStats(username));
        stats.setPriorityStats(getPriorityStats(username));

        return stats;
    }

    private void calculateTimeStatistics(UserStatisticsDto stats, List<RedmineIssue> userIssues) {
        Long totalEstimated = userIssues.stream()
                .mapToLong(issue -> issue.getEstimatedHours() != null ? issue.getEstimatedHours() : 0L)
                .sum();
        
        Long totalSpent = userIssues.stream()
                .mapToLong(issue -> issue.getSpentHours() != 0 ? issue.getSpentHours() : 0L)
                .sum();

        stats.setTotalEstimatedHours(totalEstimated);
        stats.setTotalSpentHours(totalSpent);
    }

    private List<ProjectStatsDto> getProjectStats(String username, List<RedmineIssue> userIssues) {
        Map<String, List<RedmineIssue>> projectGroups = userIssues.stream()
                .filter(issue -> issue.getProject() != null)
                .collect(Collectors.groupingBy(issue -> issue.getProject().getName()));

        List<ProjectStatsDto> projectStats = new ArrayList<>();
        
        for (Map.Entry<String, List<RedmineIssue>> entry : projectGroups.entrySet()) {
            String projectName = entry.getKey();
            List<RedmineIssue> issues = entry.getValue();
            
            Long total = (long) issues.size();
            Long completed = issues.stream()
                    .mapToLong(issue -> (issue.getStatusId() == 5 || issue.getStatusId() == 3) ? 1 : 0) // Kapatıldı veya Çözüldü
                    .sum();

            String identifier = issues.get(0).getProject().getIdentifier();
            projectStats.add(new ProjectStatsDto(projectName, identifier, total, completed));
        }
        
        // Completion rate'e göre sırala
        return projectStats.stream()
                .sorted((a, b) -> Double.compare(b.getCompletionRate(), a.getCompletionRate()))
                .collect(Collectors.toList());
    }

    private List<StatusStatsDto> getStatusStats(String username) {
        List<StatusStatsDto> statusStats = new ArrayList<>();
        
        // Redmine standart durumları - sadece 0'dan büyük olanları al
        Long newCount = issueRepository.countIssuesByUsernameAndStatus(username, 1);
        if (newCount > 0) statusStats.add(new StatusStatsDto(1, "Yeni", newCount));
        
        Long assignedCount = issueRepository.countIssuesByUsernameAndStatus(username, 2);
        if (assignedCount > 0) statusStats.add(new StatusStatsDto(2, "Atanmış", assignedCount));
        
        Long resolvedCount = issueRepository.countIssuesByUsernameAndStatus(username, 3);
        if (resolvedCount > 0) statusStats.add(new StatusStatsDto(3, "Çözüldü", resolvedCount));
        
        Long feedbackCount = issueRepository.countIssuesByUsernameAndStatus(username, 4);
        if (feedbackCount > 0) statusStats.add(new StatusStatsDto(4, "Geri Bildirim", feedbackCount));
        
        Long closedCount = issueRepository.countIssuesByUsernameAndStatus(username, 5);
        if (closedCount > 0) statusStats.add(new StatusStatsDto(5, "Kapatıldı", closedCount));
        
        Long rejectedCount = issueRepository.countIssuesByUsernameAndStatus(username, 6);
        if (rejectedCount > 0) statusStats.add(new StatusStatsDto(6, "Reddedildi", rejectedCount));
        
        return statusStats;
    }

    private List<PriorityStatsDto> getPriorityStats(String username) {
        List<PriorityStatsDto> priorityStats = new ArrayList<>();
        
        // Redmine standart öncelik seviyeleri - sadece 0'dan büyük olanları al
        Long lowCount = issueRepository.countIssuesByUsernameAndPriority(username, 1);
        if (lowCount > 0) priorityStats.add(new PriorityStatsDto(1, "Düşük", lowCount));
        
        Long normalCount = issueRepository.countIssuesByUsernameAndPriority(username, 2);
        if (normalCount > 0) priorityStats.add(new PriorityStatsDto(2, "Normal", normalCount));
        
        Long highCount = issueRepository.countIssuesByUsernameAndPriority(username, 3);
        if (highCount > 0) priorityStats.add(new PriorityStatsDto(3, "Yüksek", highCount));
        
        Long urgentCount = issueRepository.countIssuesByUsernameAndPriority(username, 4);
        if (urgentCount > 0) priorityStats.add(new PriorityStatsDto(4, "Acil", urgentCount));
        
        Long immediateCount = issueRepository.countIssuesByUsernameAndPriority(username, 5);
        if (immediateCount > 0) priorityStats.add(new PriorityStatsDto(5, "Hemen", immediateCount));
        
        return priorityStats;
    }

    public List<UserStatisticsDto> getAllUsersStats() {
        List<RedmineUser> allUsers = userRepository.findAll();
        List<UserStatisticsDto> allStats = new ArrayList<>();
        
        for (RedmineUser user : allUsers) {
            try {
                UserStatisticsDto userStats = getUserStats(user.getLogin());
                if (userStats != null) {
                    allStats.add(userStats);
                }
            } catch (Exception e) {
                // Log error but continue with other users
                System.err.println("Error calculating stats for user: " + user.getLogin() + " - " + e.getMessage());
            }
        }
        
        return allStats;
    }

    // Dashboard için özel istatistikler
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto dashboardStats = new DashboardStatsDto();
        
        List<UserStatisticsDto> allUserStats = getAllUsersStats();
        
        // Genel istatistikler
        Long totalUsers = (long) allUserStats.size();
        Long totalActiveIssues = allUserStats.stream()
                .mapToLong(stats -> stats.getActiveIssueCount() != null ? stats.getActiveIssueCount() : 0)
                .sum();
        
        Long totalOverdueIssues = allUserStats.stream()
                .mapToLong(stats -> stats.getOverdueIssueCount() != null ? stats.getOverdueIssueCount() : 0)
                .sum();
        
        Long totalCompletedIssues = allUserStats.stream()
                .mapToLong(stats -> stats.getCompletedIssueCount() != null ? stats.getCompletedIssueCount() : 0)
                .sum();

        dashboardStats.setTotalUsers(totalUsers);
        dashboardStats.setTotalActiveIssues(totalActiveIssues);
        dashboardStats.setTotalOverdueIssues(totalOverdueIssues);
        dashboardStats.setTotalCompletedIssues(totalCompletedIssues);
        
        // Ortalama completion rate
        Double avgCompletionRate = allUserStats.stream()
                .filter(stats -> stats.getCompletionRate() != null)
                .mapToDouble(UserStatisticsDto::getCompletionRate)
                .average()
                .orElse(0.0);
        
        dashboardStats.setAverageCompletionRate(avgCompletionRate);
        
        return dashboardStats;
    }

    // Performans trendi hesaplama
    public List<UserPerformanceTrendDto> getUserPerformanceTrend(String username, int weeks) {
        List<UserPerformanceTrendDto> trends = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = weeks; i >= 0; i--) {
            LocalDateTime weekEnd = now.minusWeeks(i);
            LocalDateTime weekStart = weekEnd.minusWeeks(1);
            
            Long completed = issueRepository.countCompletedIssuesInWeek(username, weekStart, weekEnd);
            Long total = issueRepository.countTotalIssues(username, weekStart, weekEnd);
            
            UserPerformanceTrendDto trend = new UserPerformanceTrendDto();
            trend.setWeekStart(weekStart.toLocalDate());
            trend.setWeekEnd(weekEnd.toLocalDate());
            trend.setCompletedIssues(completed);
            trend.setTotalIssues(total);
            trend.setCompletionRate(total > 0 ? (double) completed / total * 100 : 0.0);
            
            trends.add(trend);
        }
        
        return trends;
    }
}