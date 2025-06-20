package com.example.springProject.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "trackers")
public class RedmineTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tracker", fetch = FetchType.LAZY)
    private List<RedmineIssue> issues;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : " "; 
    }

    public void setName(String name) {
        this.name = name; 
    }

    public String getDescription() {
        return description != null ? description : " ";
    }

    public void setDescription(String description) {
        this.description = description; 
    }
}
