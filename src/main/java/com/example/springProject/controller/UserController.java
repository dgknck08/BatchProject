package com.example.springProject.controller;

import com.example.springProject.model.RedmineUser;
import com.example.springProject.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<RedmineUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{login}")
    public ResponseEntity<RedmineUser> getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
