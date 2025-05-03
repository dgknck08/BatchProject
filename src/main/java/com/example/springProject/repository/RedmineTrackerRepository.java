package com.example.springProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springProject.model.RedmineTracker;

public interface RedmineTrackerRepository extends JpaRepository<RedmineTracker,Integer>{

	Optional<RedmineTracker> findByName(String name);

}
