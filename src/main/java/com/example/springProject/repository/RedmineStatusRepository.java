// RedmineStatusRepository.java
package com.example.springProject.repository;

import com.example.springProject.model.RedmineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedmineStatusRepository extends JpaRepository<RedmineStatus, Integer> {
}

