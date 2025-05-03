package com.example.springProject.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trackers")
public class RedmineTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

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
