package com.example.springProject.repository;

import com.example.springProject.model.RedminePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedminePriorityRepository extends JpaRepository<RedminePriority, Integer> {
}