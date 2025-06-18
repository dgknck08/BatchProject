package com.example.springProject.service.api;

import com.example.springProject.model.RedmineUser;
import com.example.springProject.repository.RedmineUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private RedmineUserRepository userRepository;

    public List<RedmineUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<RedmineUser> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
