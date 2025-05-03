package com.example.springProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springProject.model.RedmineUser;



public interface RedmineUserRepository extends JpaRepository<RedmineUser,Integer>{

    Optional<RedmineUser> findByLogin(String login);
    Optional<RedmineUser> findByMail(String mail);
}
