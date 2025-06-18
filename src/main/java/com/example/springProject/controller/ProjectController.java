package com.example.springProject.controller;

import com.example.springProject.model.RedmineProject;
import com.example.springProject.service.api.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<RedmineProject> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/user/{username}")
    public List<RedmineProject> getProjectsByUsername(@PathVariable String username) {
        return projectService.getProjectsByUsername(username);
    }
}
