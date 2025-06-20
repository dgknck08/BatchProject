package com.example.springProject.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "priorities")
public class RedminePriority {

    @Id
    private int id;

    private String name;
    
    @JsonIgnore
    @OneToMany(mappedBy = "priority", fetch = FetchType.LAZY)
    private List<RedmineIssue> issues;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
    
    
}
