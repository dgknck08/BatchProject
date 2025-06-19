package com.example.springProject.service.batch;

 
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springProject.model.RedmineIssue;
import com.example.springProject.model.RedmineProject;

import com.example.springProject.model.RedmineTracker;
import com.example.springProject.model.RedmineUser;
import com.example.springProject.repository.RedmineIssueRepository;
import com.example.springProject.repository.RedmineProjectRepository;
import com.example.springProject.repository.RedmineTrackerRepository;
import com.example.springProject.repository.RedmineUserRepository;
import com.example.springProject.exception.DatabaseException;
import ch.qos.logback.classic.Logger;
 
@Service
public class DataPersistenceService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DataPersistenceService.class);
    private final RedmineTrackerRepository trackerRepository;
    private final RedmineIssueRepository issueRepository;
    private final RedmineProjectRepository projectRepository;
    private final RedmineUserRepository userRepository;

    @Autowired
    public DataPersistenceService(RedmineUserRepository userRepository,
                                  RedmineProjectRepository projectRepository,
                                  RedmineIssueRepository issueRepository,
                                  RedmineTrackerRepository trackerRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.issueRepository = issueRepository;
        this.trackerRepository = trackerRepository;
    }

    public void saveIssues(List<RedmineIssue> issues) {
        List<RedmineIssue> existingIssues = issueRepository.findAll();
        Map<Integer, RedmineIssue> existingIssueMap = existingIssues.stream()
            .collect(Collectors.toMap(RedmineIssue::getId, Function.identity()));

        // Mevcut kullanıcıları bir kere çekip login ile mapping
        Map<String, RedmineUser> existingUserMap = userRepository.findAll().stream()
            .collect(Collectors.toMap(RedmineUser::getLogin, Function.identity()));

        for (RedmineIssue issue : issues) {
            try {
                RedmineUser assignedUser = issue.getAssignedTo();
                if (assignedUser != null && assignedUser.getLogin() != null && !assignedUser.getLogin().isEmpty()) {
                    RedmineUser existingUser = existingUserMap.get(assignedUser.getLogin());
                    if (existingUser == null) {
                        userRepository.save(assignedUser);
                        existingUserMap.put(assignedUser.getLogin(), assignedUser);
                    } else {
                        issue.setAssignedTo(existingUser);
                    }
                } else {
                    // assignedUser varsa ama login boşsa sorun çıkmasın diye assignedTo null yaptım
                    issue.setAssignedTo(null);
                }

                if (existingIssueMap.containsKey(issue.getId())) {
                    RedmineIssue existing = existingIssueMap.get(issue.getId());
                    boolean isUpdated = false;

                    if (existing.getUpdatedOn() == null || issue.getUpdatedOn().isAfter(existing.getUpdatedOn())) {
                        isUpdated = true;
                        existing.setProject(issue.getProject());
                        existing.setAssignedTo(issue.getAssignedTo());
                        existing.setSubject(issue.getSubject());
                        existing.setDescription(issue.getDescription());
                        existing.setStatusId(issue.getStatusId());
                        existing.setPriorityId(issue.getPriorityId());
                        existing.setStartDate(issue.getStartDate());
                        existing.setDueDate(issue.getDueDate());
                        existing.setDoneRatio(issue.getDoneRatio());
                        existing.setPrivate(issue.isPrivate());
                        existing.setEstimatedHours(issue.getEstimatedHours());
                        existing.setSpentHours(issue.getSpentHours());
                        existing.setCreatedOn(issue.getCreatedOn());
                        existing.setUpdatedOn(issue.getUpdatedOn());
                        existing.setClosedOn(issue.getClosedOn());

                        logger.info("Updated issue: ID: {}", existing.getId());
                    } else {
                        logger.info("Issue ID: {} is already up-to-date", existing.getId());
                    }

                    if (isUpdated) {
                        issueRepository.save(existing);
                    }
                } else {
                    issueRepository.save(issue);
                    logger.info("New issue saved: ID: {}", issue.getId());
                }
            } catch (Exception e) {
                String query = "Saving issue with ID: " + issue.getId();
                logger.error("Error occurred while saving issue: {}", e.getMessage());
                logger.error("Failed Query: {}", query);
                logger.error("StackTrace: ", e);
                throw new DatabaseException("Error occurred while saving issue", query, e);
            }
        }
    }



    public void saveTrackers(List<RedmineTracker> trackers) {
        List<RedmineTracker> existingTrackers = trackerRepository.findAll();
        Map<Integer, RedmineTracker> existingTrackerMap = existingTrackers.stream()
            .collect(Collectors.toMap(RedmineTracker::getId, Function.identity()));

        for (RedmineTracker tracker : trackers) {
            try {
                if (existingTrackerMap.containsKey(tracker.getId())) {
                    RedmineTracker existing = existingTrackerMap.get(tracker.getId());
                    boolean isUpdated = false;

                    if (existing.getDescription() == null || !existing.getDescription().equals(tracker.getDescription())) {
                        existing.setDescription(tracker.getDescription());
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        trackerRepository.save(existing);
                        logger.info("Updated tracker: {}", existing.getName());
                    } else {
                        logger.info("Tracker name: {} is already up-to-date", existing.getName());
                    }
                } else {
                    trackerRepository.save(tracker);
                    logger.info("New tracker saved: {}", tracker.getName());
                }
            } catch (Exception e) {
                String query = "Saving tracker with ID: " + tracker.getId(); 
                logger.error("Database error occurred: {}", e.getMessage());
                logger.error("Failed Query: {}", query);
                logger.error("StackTrace: ", e);
                throw new DatabaseException("Error occurred while saving tracker", query, e);
            }
        }
    }

    public void saveProjects(List<RedmineProject> projects) {
        Map<String, RedmineProject> projectMap = projects.stream()
            .collect(Collectors.toMap(RedmineProject::getIdentifier, Function.identity()));

        for (Map.Entry<String, RedmineProject> entry : projectMap.entrySet()) {
            RedmineProject project = entry.getValue();

            try {
                Optional<RedmineProject> existingProject = projectRepository.findByIdentifier(project.getIdentifier());

                if (existingProject.isPresent()) {
                    RedmineProject existing = existingProject.get();
                    boolean isUpdated = false;

                    // Proje açıklamasını 
                    if (!existing.getDescription().equals(project.getDescription())) {
                        existing.setDescription(project.getDescription());
                        isUpdated = true;
                    }

                    // Proje durumunu kontrolü
                    if (existing.getStatus() != project.getStatus()) {
                        existing.setStatus(project.getStatus());
                        isUpdated = true;
                    }

                    // Proje erişim durumunu kontrol et ve güncelleme işşlemi
                    if (existing.isPublic() != project.isPublic()) {
                        existing.setPublic(project.isPublic());
                        isUpdated = true;
                    }

                    // güncellendiyse kaydetme işlemi
                    if (isUpdated) {
                        projectRepository.save(existing);
                        logger.info("Updated project: {}", project.getIdentifier());
                    } else {
                        logger.info("Project Identifier: {} is already up-to-date", project.getIdentifier());
                    }
                } else {
                    projectRepository.save(project);
                    logger.info("New project saved: {}", project.getIdentifier());
                }
            } catch (Exception e) {
                String query = "Saving project with Identifier: " + project.getIdentifier(); // Sorgu bilgisi
                logger.error("Database error occurred: {}", e.getMessage());
                logger.error("Failed Query: {}", query);
                logger.error("StackTrace: ", e);
                throw new DatabaseException("Error occurred while saving project", query, e);
            }
        }
    }

    public void saveUsers(List<RedmineUser> users) {
        try {
            List<RedmineUser> existingUsers = userRepository.findAll();
            Map<String, RedmineUser> existingUserMap = existingUsers.stream()
                .collect(Collectors.toMap(RedmineUser::getLogin, Function.identity()));

            for (RedmineUser user : users) {
                try {
                    if (existingUserMap.containsKey(user.getLogin())) {
                        RedmineUser existingUser = existingUserMap.get(user.getLogin());
                        boolean isUpdated = false;

                        if (!existingUser.getFirstname().equals(user.getFirstname())) {
                            existingUser.setFirstname(user.getFirstname());
                            isUpdated = true;
                        }
                        if (!existingUser.getLastname().equals(user.getLastname())) {
                            existingUser.setLastname(user.getLastname());
                            isUpdated = true;
                        }
                        if (!existingUser.getMail().equals(user.getMail())) {
                            existingUser.setMail(user.getMail());
                            isUpdated = true;
                        }
                        if (existingUser.isAdmin() != user.isAdmin()) {
                            existingUser.setAdmin(user.isAdmin());
                            isUpdated = true;
                        }

                        if (isUpdated) {
                            userRepository.save(existingUser);
                            logger.info("Updated user: {}", user.getLogin());
                        } else {
                            logger.info("User login: {} is already up-to-date", user.getLogin());
                        }
                    } else {
                        userRepository.save(user);
                        logger.info("New user saved: {}", user.getLogin());
                    }
                } catch (Exception e) {
                    String query = "Saving user with login: " + user.getLogin(); 
                    logger.error("Database error occurred while processing user {}: {}", user.getLogin(), e.getMessage());
                    logger.error("Failed Query: {}", query);
                    logger.error("StackTrace: ", e);
                    throw new DatabaseException("Error occurred while saving user", query, e);
                }
            }
        } catch (Exception e) {
            logger.error("Database error: {}", e.getMessage());
            throw new DatabaseException("Error occurred while saving users: " + e.getMessage(), e);
        }
    }

    public void deleteNonMatchingUsers(List<RedmineUser> users) {
        List<RedmineUser> existingUsers = userRepository.findAll();
        for (RedmineUser existingUser : existingUsers) {
            boolean found = users.stream()
                .anyMatch(user -> user.getLogin().equals(existingUser.getLogin()));
            if (!found) {
                userRepository.delete(existingUser);
                logger.info("Deleted user: {}", existingUser.getLogin());
            }
        }
    }

    public void deleteNonMatchingProjects(List<RedmineProject> projects) {
        List<RedmineProject> existingProjects = projectRepository.findAll();
        for (RedmineProject existingProject : existingProjects) {
            boolean found = projects.stream()
                .anyMatch(project -> project.getIdentifier().equals(existingProject.getIdentifier()));
            if (!found) {
                projectRepository.delete(existingProject);
                logger.info("Deleted project: {}", existingProject.getIdentifier());
            }
        }
    }

    public void deleteNonMatchingTrackers(List<RedmineTracker> trackers) {
        List<RedmineTracker> existingTrackers = trackerRepository.findAll();
        for (RedmineTracker existingTracker : existingTrackers) {
            boolean found = trackers.stream()
                .anyMatch(tracker -> tracker.getId() == existingTracker.getId());
            if (!found) {
                trackerRepository.delete(existingTracker);
                logger.info("Deleted tracker: {}", existingTracker.getName());
            }
        }
    }

    public void deleteNonMatchingIssues(List<RedmineIssue> issues) {
        List<RedmineIssue> existingIssues = issueRepository.findAll();
        for (RedmineIssue existingIssue : existingIssues) {
            boolean found = issues.stream()
                .anyMatch(issue -> issue.getId() == existingIssue.getId());
            if (!found) {
                issueRepository.delete(existingIssue);
                logger.info("Deleted issue: {}", existingIssue.getSubject());
            }
        }
    }
}
