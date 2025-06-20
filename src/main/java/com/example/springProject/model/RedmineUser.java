package com.example.springProject.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class RedmineUser {

    @Id
    @JsonProperty("id")
    private int id;

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
    private OffsetDateTime createdOn;

    @JsonProperty("updated_on")
    private OffsetDateTime updatedOn;

    @JsonProperty("last_login_on")
    private OffsetDateTime lastLoginOn;

    @JsonProperty("passwd_changed_on")
    private OffsetDateTime passwdChangedOn;

    @JsonProperty("twofa_scheme")
    private String twofaScheme;

    
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<RedmineIssue> assignedIssues;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<RedmineIssue> authoredIssues;


    // Getters and Setters

    @JsonProperty("name")
    public String getName() {
        StringBuilder nameBuilder = new StringBuilder();
        if (firstname != null && !firstname.isEmpty()) {
            nameBuilder.append(firstname);
        }
        if (lastname != null && !lastname.isEmpty()) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(" ");
            }
            nameBuilder.append(lastname);
        }
        return nameBuilder.toString();
    }

    // Name setter için (JSON deserialization için)
    @JsonProperty("name")
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            String[] nameParts = name.trim().split(" ", 2);
            
            if (nameParts.length > 0) {
                this.firstname = nameParts[0];
            }
            if (nameParts.length > 1) {
                this.lastname = nameParts[1];
            }
            
            // Eğer login null ise, name'den oluştur
            if (this.login == null || this.login.isEmpty()) {
                this.login = name.replaceAll(" ", ".").toLowerCase();
            }
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public OffsetDateTime getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(OffsetDateTime lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public OffsetDateTime getPasswdChangedOn() {
        return passwdChangedOn;
    }

    public void setPasswdChangedOn(OffsetDateTime passwdChangedOn) {
        this.passwdChangedOn = passwdChangedOn;
    }

    public String getTwofaScheme() {
        return twofaScheme != null ? twofaScheme : "";
    }

    public void setTwofaScheme(String twofaScheme) {
        this.twofaScheme = twofaScheme != null ? twofaScheme : "";
    }

    public List<RedmineIssue> getIssues() {
        return assignedIssues;
    }

    public void setIssues(List<RedmineIssue> assignedIssues) {
        this.assignedIssues = assignedIssues;
    }
}
