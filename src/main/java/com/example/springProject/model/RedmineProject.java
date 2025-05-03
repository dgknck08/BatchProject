package com.example.springProject.model;



import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "projects") // Veritabanındaki tablo adı
public class RedmineProject {

    @Id
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("description")
    private String description;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("status")
    private int status;

    @JsonProperty("is_public")
    private boolean isPublic;

    @JsonProperty("inherit_members")
    private boolean inheritMembers;

    @JsonProperty("created_on")
    private LocalDateTime createdOn;

    @JsonProperty("updated_on")
    private LocalDateTime updatedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RedmineIssue> issues;


    public List<RedmineIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<RedmineIssue> issues) {
        this.issues = issues;
    }

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

    public String getIdentifier() {
        return identifier != null ? identifier : " "; 
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier; 
    }

    public String getDescription() {
        return description != null ? description : " "; 
    }

    public void setDescription(String description) {
        this.description = description; 
    }

    public String getHomepage() {
        return homepage != null ? homepage : " "; 
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isInheritMembers() {
        return inheritMembers;
    }

    public void setInheritMembers(boolean inheritMembers) {
        this.inheritMembers = inheritMembers;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}

