package com.example.springProject.repository;

import com.example.springProject.model.RedmineIssue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RedmineIssueRepository extends JpaRepository<RedmineIssue, Integer> {

    // Tamamlanan işler (statusId = 5)
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.statusId = 5 AND i.assignedTo.login = :username AND i.closedDate BETWEEN :startDate AND :endDate")
    Long countCompletedIssuesInWeek(@Param("username") String username, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Toplam işler
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.createdOn BETWEEN :startDate AND :endDate")
    Long countTotalIssues(@Param("username") String username, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Dashboard için detaylı istatistikler
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :username")
    List<RedmineIssue> findAllByUsername(@Param("username") String username);

    // Proje bazında istatistikler
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.project.id = :projectId")
    Long countIssuesByUsernameAndProject(@Param("username") String username, @Param("projectId") int projectId);

    // Duruma göre iş sayısı
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.statusId = :statusId")
    Long countIssuesByUsernameAndStatus(@Param("username") String username, @Param("statusId") int statusId);

    // Önceliğe göre iş sayısı
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.priorityId = :priorityId")
    Long countIssuesByUsernameAndPriority(@Param("username") String username, @Param("priorityId") int priorityId);

    // Kullanıcının aktif işleri
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.statusId NOT IN (3, 5, 6)")
    List<RedmineIssue> findActiveIssuesByUsername(@Param("username") String username);

    // Geciken işler
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.dueDate < CURRENT_DATE AND i.statusId NOT IN (3, 5, 6)")
    List<RedmineIssue> findOverdueIssuesByUsername(@Param("username") String username);

    Optional<RedmineIssue> findById(int id);
}