package com.example.springProject.repository;


import com.example.springProject.model.RedmineIssue;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RedmineIssueRepository extends JpaRepository<RedmineIssue, Integer> {


    @Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.statusId = 5 AND i.assignedTo.login = :username AND i.closedDate BETWEEN :startDate AND :endDate")
    Long countCompletedIssuesInWeek(@Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
	Optional<RedmineIssue> findById(int id);

	@Query("SELECT COUNT(i) FROM RedmineIssue i WHERE i.assignedTo.login = :username AND i.createdOn BETWEEN :startDate AND :endDate")
	Long countTotalIssues(@Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
  
}
