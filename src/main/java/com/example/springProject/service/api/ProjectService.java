package com.example.springProject.service.api;

import com.example.springProject.model.RedmineProject;
import com.example.springProject.repository.RedmineProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private RedmineProjectRepository projectRepository;

    public List<RedmineProject> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<RedmineProject> getProjectsByUsername(String username) {
        return projectRepository.findProjectsByUsername(username);
    }
}
