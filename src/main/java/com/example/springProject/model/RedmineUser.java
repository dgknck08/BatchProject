package com.example.springProject.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class RedmineUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    private String username;

    @JsonProperty("login")
    @Column(unique = true, nullable = false)
    private String login;

    @JsonProperty("admin")
    private boolean admin;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("created_on")
    private LocalDateTime createdOn;

    @JsonProperty("updated_on")
    private LocalDateTime updatedOn;

    @JsonProperty("last_login_on")
    private LocalDateTime lastLoginOn;

    @JsonProperty("passwd_changed_on")
    private LocalDateTime passwdChangedOn;

    @JsonProperty("twofa_scheme")
    private String twofaScheme;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RedmineIssue> issues;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username != null ? username : "";
    }

    public void setUsername(String username) {
        this.username = username != null ? username : "";
    }

    public String getLogin() {
        return login != null ? login : "";
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login cannot be null or empty");
        }
        this.login = login;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname != null ? firstname : "";
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname != null ? firstname : "";
    }

    public String getLastname() {
        return lastname != null ? lastname : "";
    }

    public void setLastname(String lastname) {
        this.lastname = lastname != null ? lastname : "";
    }

    public String getMail() {
        return mail != null ? mail : "";
    }

    public void setMail(String mail) {
        this.mail = mail != null ? mail : "";
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

    public LocalDateTime getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(LocalDateTime lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public LocalDateTime getPasswdChangedOn() {
        return passwdChangedOn;
    }

    public void setPasswdChangedOn(LocalDateTime passwdChangedOn) {
        this.passwdChangedOn = passwdChangedOn;
    }

    public String getTwofaScheme() {
        return twofaScheme != null ? twofaScheme : "";
    }

    public void setTwofaScheme(String twofaScheme) {
        this.twofaScheme = twofaScheme != null ? twofaScheme : "";
    }

    public List<RedmineIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<RedmineIssue> issues) {
        this.issues = issues;
    }
}
