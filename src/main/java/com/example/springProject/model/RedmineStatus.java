package com.example.springProject.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="status")
public class RedmineStatus {
	@Id
	private int id;
	
	
	private String name;
	
	@JsonProperty("is_closed")
	private boolean isClosed;
	
	@JsonIgnore
    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
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

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	
	
	
	
}
