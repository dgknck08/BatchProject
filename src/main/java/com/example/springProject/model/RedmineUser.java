package com.example.springProject.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class RedmineUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    private String username;

    // Getter
    public String getUsername() {
        return username;
    }

    // Setter
    public void setUsername(String username) {
        this.username = username;
    }

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
    
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // RedmineSorun'daki proje alanına göre
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

    public String getLogin() {
        return login; // null ise boş string döner
    }

    public void setLogin(String login) {
        this.login = login; // null ise boş string atar
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname != null ? firstname : " "; // null ise boş string döner
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname != null ? firstname : " "; // null ise boş string atar
    }

    public String getLastname() {
        return lastname != null ? lastname : " "; // null ise boş string döner
    }

    public void setLastname(String lastname) {
        this.lastname = lastname != null ? lastname : " "; // null ise boş string atar
    }

    public String getMail() {
        return mail != null ? mail : " "; // null ise boş string döner
    }

    public void setMail(String mail) {
        this.mail = mail != null ? mail : " "; // null ise boş string atar
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
        return twofaScheme != null ? twofaScheme : " "; // null ise boş string döner
    }

    public void setTwofaScheme(String twofaScheme) {
        this.twofaScheme = twofaScheme != null ? twofaScheme : " "; // null ise boş string atar
    }
}

	
	

