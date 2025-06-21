package com.example.springProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springProject.model.RedmineProject;

public interface RedmineProjectRepository extends JpaRepository<RedmineProject,Integer>{
	  Optional<RedmineProject> findByName(String name);
	  Optional<RedmineProject> findByIdentifier(String identifier);
	  @Query("SELECT DISTINCT p FROM RedmineProject p " +
	           "JOIN RedmineIssue i ON i.project.id = p.id " +
	           "WHERE i.assignedTo.login = :username")
	    List<RedmineProject> findProjectsByUsername(@Param("username") String username);

}
