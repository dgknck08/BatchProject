package com.example.springProject.repository;

import com.example.springProject.model.RedmineIssue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RedmineIssueRepository extends JpaRepository<RedmineIssue, Integer> {

    // Belirtilen kullanıcı için, verilen tarih aralığında kapatılmış (status.id=5) olan işlerin sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.status.id = 5 AND i.assignedTo.login = :login AND i.closedDate BETWEEN :startDate AND :endDate")
    Long countCompletedIssuesInWeek(@Param("login") String login,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    // Belirtilen kullanıcı için, verilen tarih aralığında oluşturulan tüm işlerin sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.createdOn BETWEEN :startDate AND :endDate")
    Long countTotalIssues(@Param("login") String login,
                          @Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);
    
    // Belirtilen kullanıcıya atanmış tüm işleri döner
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :login")
    List<RedmineIssue> findAllByLogin(@Param("login") String login);

    // Belirtilen kullanıcı ve proje için iş sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.project.id = :projectId")
    Long countIssuesByLoginAndProject(@Param("login") String login,
                                      @Param("projectId") int projectId);

    // Belirtilen kullanıcı ve duruma (status) göre iş sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.status.id = :statusId")
    Long countIssuesByLoginAndStatus(@Param("login") String login,
                                     @Param("statusId") int statusId);

    // Belirtilen kullanıcı ve önceliğe göre iş sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.priority.id = :priorityId")
    Long countIssuesByLoginAndPriority(@Param("login") String login,
                                       @Param("priorityId") int priorityId);

    // Belirtilen kullanıcı için aktif olan (status 3,5,6 dışındaki) işleri döner
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.status.id NOT IN (3, 5, 6)")
    List<RedmineIssue> findActiveIssuesByLogin(@Param("login") String login);

    // Belirtilen kullanıcı için, teslim tarihi geçmiş ve halen kapatılmamış işler (status 3,5,6 dışındaki) döner
    @Query("SELECT i FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.dueDate < CURRENT_DATE AND i.status.id NOT IN (3, 5, 6)")
    List<RedmineIssue> findOverdueIssuesByLogin(@Param("login") String login);

    // Belirtilen durum id'lerine sahip işler için toplam sayıyı döner (tüm kullanıcılar için)
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.status.id IN :statusIds")
    Long countByStatusIdIn(@Param("statusIds") List<Integer> statusIds);

    // Belirtilen kullanıcı ve durum id'leri için iş sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login AND i.status.id IN :statusIds")
    Long countByLoginAndStatusIdIn(@Param("login") String login, @Param("statusIds") List<Integer> statusIds);

    // Belirtilen kullanıcı için, verilen tarih aralığında ve belirtilen durumlara sahip işlerin sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login " +
           "AND i.updatedOn >= :startDate AND i.updatedOn <= :endDate " +
           "AND i.status.id IN :statusIds")
    Long countIssuesInDateRangeByLoginAndStatusIn(@Param("login") String login, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate,
                                                 @Param("statusIds") List<Integer> statusIds);

    // Belirtilen kullanıcı için atandığı tüm işlerin toplam sayısını döner
    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :login")
    Long countTotalIssuesByLogin(@Param("login") String login);

    // ID ile iş bulur
    Optional<RedmineIssue> findById(int id);
}
