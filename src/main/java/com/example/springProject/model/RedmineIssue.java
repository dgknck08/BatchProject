package com.example.springProject.model;



import java.time.LocalDate;
import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "issues")
public class RedmineIssue {

    @Id
    @JsonProperty("id")
    private int id;

    @JsonProperty("project")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private RedmineProject project;

    @JsonProperty("assigned_to")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private RedmineUser assignedTo;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "closed_date") // Optional if the column name matches
    private LocalDateTime closedDate; // Assuming it's a Date or LocalDateTime type

    // Getter and setter for closedDate
    public LocalDateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
    }

    @JsonProperty("due_date")
    private String dueDate;

    @JsonProperty("done_ratio")
    private int doneRatio;

    @JsonProperty("is_private")
    private boolean isPrivate;

    @JsonProperty("estimated_hours")
    private Integer estimatedHours;

    @JsonProperty("spent_hours")
    private int spentHours;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("updated_on")
    private LocalDateTime updatedOn;

    @JsonProperty("closed_on")
    private LocalDateTime closedOn;

    @JsonProperty("statusid")
	private int statusId;

    @JsonProperty("priorityid")
	private int priorityId;

    // Getter ve Setter metodları
    public RedmineProject getProject() {
        return project;
    }

    public void setProject(RedmineProject project) {
        this.project = project;
    }

    public RedmineUser getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(RedmineUser assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject != null ? subject : " "; // null ise boş string döner
    }

    public void setSubject(String subject) {
        this.subject = subject != null ? subject : " "; // null ise boş string atar
    }

    public String getDescription() {
        return description != null ? description : " "; // null ise boş string döner
    }

    public void setDescription(String description) {
        this.description = description != null ? description : " "; // null ise boş string atar
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getDoneRatio() {
        return doneRatio;
    }

    public void setDoneRatio(int doneRatio) {
        this.doneRatio = doneRatio;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public int getSpentHours() {
        return spentHours;
    }

    public void setSpentHours(int spentHours) {
        this.spentHours = spentHours;
    }

   

    public String getCreatedOn() {
        return createdOn != null ? createdOn : " "; // null ise boş string döner
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn != null ? createdOn : " "; // null ise boş string atar
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public LocalDateTime getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(LocalDateTime closedOn) {
        this.closedOn = closedOn;
    }

	public Object getStartDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStartDate(Object startDate2) {
		// TODO Auto-generated method stub
		
	}

	public Object getDueDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDueDate(Object dueDate2) {
		// TODO Auto-generated method stub
		
	}
    
}

