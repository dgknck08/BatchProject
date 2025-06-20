package com.example.springProject.service.batch;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springProject.model.*;
import com.example.springProject.repository.*;
import com.example.springProject.exception.DatabaseException;

import ch.qos.logback.classic.Logger;

@Service
@Transactional
public class DataPersistenceService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DataPersistenceService.class);
    
    private final RedmineTrackerRepository trackerRepository;
    private final RedmineIssueRepository issueRepository;
    private final RedmineProjectRepository projectRepository;
    private final RedmineUserRepository userRepository;
    private final RedmineStatusRepository statusRepository;
    private final RedminePriorityRepository priorityRepository;

    @Autowired
    public DataPersistenceService(RedmineUserRepository userRepository,
                                  RedmineProjectRepository projectRepository,
                                  RedmineIssueRepository issueRepository,
                                  RedmineTrackerRepository trackerRepository,
                                  RedmineStatusRepository statusRepository,
                                  RedminePriorityRepository priorityRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.issueRepository = issueRepository;
        this.trackerRepository = trackerRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
    }

    public void saveIssues(List<RedmineIssue> issues) {
        if (issues == null || issues.isEmpty()) {
            logger.warn("No issues to save");
            return;
        }

        try {
            // Cache existing entities to avoid repeated database queries
            Map<Integer, RedmineIssue> existingIssueMap = issueRepository.findAll().stream()
                .collect(Collectors.toMap(RedmineIssue::getId, Function.identity()));
            
            Map<Integer, RedmineProject> existingProjectMap = projectRepository.findAll().stream()
                .collect(Collectors.toMap(RedmineProject::getId, Function.identity()));
            
            Map<Integer, RedmineUser> existingUserMap = userRepository.findAll().stream()
                .collect(Collectors.toMap(RedmineUser::getId, Function.identity()));
            
            Map<Integer, RedmineTracker> existingTrackerMap = trackerRepository.findAll().stream()
                .collect(Collectors.toMap(RedmineTracker::getId, Function.identity()));
            
            Map<Integer, RedmineStatus> existingStatusMap = statusRepository.findAll().stream()
                .collect(Collectors.toMap(RedmineStatus::getId, Function.identity()));
            
            Map<Integer, RedminePriority> existingPriorityMap = priorityRepository.findAll().stream()
                .collect(Collectors.toMap(RedminePriority::getId, Function.identity()));

            for (RedmineIssue issue : issues) {
                try {
                    // Process nested entities
                    processNestedEntities(issue, existingProjectMap, existingUserMap, 
                                        existingTrackerMap, existingStatusMap, existingPriorityMap);
                    
                    // Save or update the issue
                    saveOrUpdateIssue(issue, existingIssueMap);
                } catch (Exception e) {
                    handleIssueException(issue, e);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing issues batch: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while processing issues batch", "saveIssues", e);
        }
    }

    private void processNestedEntities(RedmineIssue issue,
                                     Map<Integer, RedmineProject> projectMap,
                                     Map<Integer, RedmineUser> userMap,
                                     Map<Integer, RedmineTracker> trackerMap,
                                     Map<Integer, RedmineStatus> statusMap,
                                     Map<Integer, RedminePriority> priorityMap) {
        
        // Process Project
        if (issue.getProject() != null && issue.getProject().getId() > 0) {
            RedmineProject existingProject = projectMap.get(issue.getProject().getId());
            if (existingProject != null) {
                issue.setProject(existingProject);
            } else {
                // Create and save new project if it doesn't exist
                RedmineProject newProject = issue.getProject();
                projectRepository.save(newProject);
                projectMap.put(newProject.getId(), newProject);
                logger.debug("Created new project: {}", newProject.getName());
            }
        }

        // Process Assigned User
        if (issue.getAssignedTo() != null && issue.getAssignedTo().getId() > 0) {
            RedmineUser existingUser = userMap.get(issue.getAssignedTo().getId());
            if (existingUser != null) {
                issue.setAssignedTo(existingUser);
            } else {
                // Create and save new user if it doesn't exist
                RedmineUser newUser = issue.getAssignedTo();
                // Set a default login if not provided (required field)
                if (newUser.getLogin() == null || newUser.getLogin().isEmpty()) {
                    newUser.setLogin("user_" + newUser.getId());
                }
                userRepository.save(newUser);
                userMap.put(newUser.getId(), newUser);
                logger.debug("Created new user: {}", newUser.getLogin());
            }
        }

        // Process Author
        if (issue.getAuthor() != null && issue.getAuthor().getId() > 0) {
            RedmineUser existingAuthor = userMap.get(issue.getAuthor().getId());
            if (existingAuthor != null) {
                issue.setAuthor(existingAuthor);
            } else {
                // Create and save new author if it doesn't exist
                RedmineUser newAuthor = issue.getAuthor();
                if (newAuthor.getLogin() == null || newAuthor.getLogin().isEmpty()) {
                    newAuthor.setLogin("author_" + newAuthor.getId());
                }
                userRepository.save(newAuthor);
                userMap.put(newAuthor.getId(), newAuthor);
                logger.debug("Created new author: {}", newAuthor.getLogin());
            }
        }

        // Process Tracker
        if (issue.getTracker() != null && issue.getTracker().getId() > 0) {
            RedmineTracker existingTracker = trackerMap.get(issue.getTracker().getId());
            if (existingTracker != null) {
                issue.setTracker(existingTracker);
            } else {
                // Create and save new tracker if it doesn't exist
                RedmineTracker newTracker = issue.getTracker();
                trackerRepository.save(newTracker);
                trackerMap.put(newTracker.getId(), newTracker);
                logger.debug("Created new tracker: {}", newTracker.getName());
            }
        }

        // Process Status
        if (issue.getStatus() != null && issue.getStatus().getId() > 0) {
            RedmineStatus existingStatus = statusMap.get(issue.getStatus().getId());
            if (existingStatus != null) {
                issue.setStatus(existingStatus);
            } else {
                // Create and save new status if it doesn't exist
                RedmineStatus newStatus = issue.getStatus();
                statusRepository.save(newStatus);
                statusMap.put(newStatus.getId(), newStatus);
                logger.debug("Created new status: {}", newStatus.getName());
            }
        }

        // Process Priority
        if (issue.getPriority() != null && issue.getPriority().getId() > 0) {
            RedminePriority existingPriority = priorityMap.get(issue.getPriority().getId());
            if (existingPriority != null) {
                issue.setPriority(existingPriority);
            } else {
                // Create and save new priority if it doesn't exist
                RedminePriority newPriority = issue.getPriority();
                priorityRepository.save(newPriority);
                priorityMap.put(newPriority.getId(), newPriority);
                logger.debug("Created new priority: {}", newPriority.getName());
            }
        }
    }

    private void saveOrUpdateIssue(RedmineIssue issue, Map<Integer, RedmineIssue> existingIssueMap) {
        if (existingIssueMap.containsKey(issue.getId())) {
            RedmineIssue existing = existingIssueMap.get(issue.getId());
            
            if (shouldUpdateIssue(existing, issue)) {
                updateIssueFields(existing, issue);
                issueRepository.save(existing);
                logger.debug("Updated issue: ID: {}", existing.getId());
            } else {
                logger.debug("Issue ID: {} is already up-to-date", existing.getId());
            }
        } else {
            issueRepository.save(issue);
            logger.debug("New issue saved: ID: {}", issue.getId());
        }
    }

    private boolean shouldUpdateIssue(RedmineIssue existing, RedmineIssue newIssue) {
        return existing.getUpdatedOn() == null || 
               (newIssue.getUpdatedOn() != null && newIssue.getUpdatedOn().isAfter(existing.getUpdatedOn()));
    }

    private void updateIssueFields(RedmineIssue existing, RedmineIssue newIssue) {
        existing.setProject(newIssue.getProject());
        existing.setAssignedTo(newIssue.getAssignedTo());
        existing.setAuthor(newIssue.getAuthor());
        existing.setTracker(newIssue.getTracker());
        existing.setSubject(newIssue.getSubject());
        existing.setDescription(newIssue.getDescription());
        existing.setStatus(newIssue.getStatus());
        existing.setPriority(newIssue.getPriority());
        existing.setStartDate(newIssue.getStartDate());
        existing.setDueDate(newIssue.getDueDate());
        existing.setDoneRatio(newIssue.getDoneRatio());
        existing.setPrivate(newIssue.isPrivate());
        existing.setEstimatedHours(newIssue.getEstimatedHours());
        existing.setSpentHours(newIssue.getSpentHours());
        existing.setCreatedOn(newIssue.getCreatedOn());
        existing.setUpdatedOn(newIssue.getUpdatedOn());
        existing.setClosedOn(newIssue.getClosedOn());
    }


    private void handleIssueException(RedmineIssue issue, Exception e) {
        String query = "Saving issue with ID: " + issue.getId();
        logger.error("Error occurred while saving issue ID {}: {}", issue.getId(), e.getMessage());
        logger.error("Failed Query: {}", query);
        logger.error("StackTrace: ", e);
        throw new DatabaseException("Error occurred while saving issue", query, e);
    }

    // Keep all your existing methods for saving other entities
    public void saveTrackers(List<RedmineTracker> trackers) {
        if (trackers == null || trackers.isEmpty()) {
            logger.warn("No trackers to save");
            return;
        }

        try {
            List<RedmineTracker> existingTrackers = trackerRepository.findAll();
            Map<Integer, RedmineTracker> existingTrackerMap = existingTrackers.stream()
                .collect(Collectors.toMap(RedmineTracker::getId, Function.identity()));

            for (RedmineTracker tracker : trackers) {
                try {
                    saveOrUpdateTracker(tracker, existingTrackerMap);
                } catch (Exception e) {
                    handleTrackerException(tracker, e);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing trackers batch: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while processing trackers batch", "saveTrackers", e);
        }
    }

    private void saveOrUpdateTracker(RedmineTracker tracker, Map<Integer, RedmineTracker> existingTrackerMap) {
        if (existingTrackerMap.containsKey(tracker.getId())) {
            RedmineTracker existing = existingTrackerMap.get(tracker.getId());
            
            if (shouldUpdateTracker(existing, tracker)) {
                existing.setName(tracker.getName());
                existing.setDescription(tracker.getDescription());
                trackerRepository.save(existing);
                logger.debug("Updated tracker: {}", existing.getName());
            } else {
                logger.debug("Tracker name: {} is already up-to-date", existing.getName());
            }
        } else {
            trackerRepository.save(tracker);
            logger.debug("New tracker saved: {}", tracker.getName());
        }
    }

    private boolean shouldUpdateTracker(RedmineTracker existing, RedmineTracker newTracker) {
        return !Objects.equals(existing.getName(), newTracker.getName()) ||
               !Objects.equals(existing.getDescription(), newTracker.getDescription());
    }

    private void handleTrackerException(RedmineTracker tracker, Exception e) {
        String query = "Saving tracker with ID: " + tracker.getId();
        logger.error("Database error occurred while saving tracker ID {}: {}", tracker.getId(), e.getMessage());
        logger.error("Failed Query: {}", query);
        logger.error("StackTrace: ", e);
        throw new DatabaseException("Error occurred while saving tracker", query, e);
    }

    public void saveProjects(List<RedmineProject> projects) {
        if (projects == null || projects.isEmpty()) {
            logger.warn("No projects to save");
            return;
        }

        try {
            Map<String, RedmineProject> projectMap = projects.stream()
                .filter(p -> p.getIdentifier() != null && !p.getIdentifier().trim().isEmpty())
                .collect(Collectors.toMap(
                    RedmineProject::getIdentifier, 
                    Function.identity(),
                    (existing, replacement) -> replacement
                ));

            for (RedmineProject project : projectMap.values()) {
                try {
                    saveOrUpdateProject(project);
                } catch (Exception e) {
                    handleProjectException(project, e);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing projects batch: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while processing projects batch", "saveProjects", e);
        }
    }

    private void saveOrUpdateProject(RedmineProject project) {
        Optional<RedmineProject> existingProject = projectRepository.findByIdentifier(project.getIdentifier());

        if (existingProject.isPresent()) {
            RedmineProject existing = existingProject.get();
            
            if (shouldUpdateProject(existing, project)) {
                updateProjectFields(existing, project);
                projectRepository.save(existing);
                logger.debug("Updated project: {}", project.getIdentifier());
            } else {
                logger.debug("Project Identifier: {} is already up-to-date", project.getIdentifier());
            }
        } else {
            projectRepository.save(project);
            logger.debug("New project saved: {}", project.getIdentifier());
        }
    }

    private boolean shouldUpdateProject(RedmineProject existing, RedmineProject newProject) {
        return !Objects.equals(existing.getName(), newProject.getName()) ||
               !Objects.equals(existing.getDescription(), newProject.getDescription()) ||
               existing.getStatus() != newProject.getStatus() ||
               existing.isPublic() != newProject.isPublic();
    }

    private void updateProjectFields(RedmineProject existing, RedmineProject newProject) {
        existing.setName(newProject.getName());
        existing.setDescription(newProject.getDescription());
        existing.setStatus(newProject.getStatus());
        existing.setPublic(newProject.isPublic());
        existing.setUpdatedOn(newProject.getUpdatedOn());
    }

    private void handleProjectException(RedmineProject project, Exception e) {
        String query = "Saving project with Identifier: " + project.getIdentifier();
        logger.error("Database error occurred while saving project {}: {}", project.getIdentifier(), e.getMessage());
        logger.error("Failed Query: {}", query);
        logger.error("StackTrace: ", e);
        throw new DatabaseException("Error occurred while saving project", query, e);
    }

    public void saveUsers(List<RedmineUser> users) {
        if (users == null || users.isEmpty()) {
            logger.warn("No users to save");
            return;
        }

        try {
            List<RedmineUser> existingUsers = userRepository.findAll();
            Map<String, RedmineUser> existingUserMap = existingUsers.stream()
                .filter(user -> user.getLogin() != null && !user.getLogin().isEmpty())
                .collect(Collectors.toMap(RedmineUser::getLogin, Function.identity()));

            for (RedmineUser user : users) {
                try {
                    if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
                        logger.warn("Skipping user with empty login");
                        continue;
                    }
                    saveOrUpdateUser(user, existingUserMap);
                } catch (Exception e) {
                    handleUserException(user, e);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing users batch: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while processing users batch", "saveUsers", e);
        }
    }

    private void saveOrUpdateUser(RedmineUser user, Map<String, RedmineUser> existingUserMap) {
        if (existingUserMap.containsKey(user.getLogin())) {
            RedmineUser existingUser = existingUserMap.get(user.getLogin());
            
            if (shouldUpdateUser(existingUser, user)) {
                updateUserFields(existingUser, user);
                userRepository.save(existingUser);
                logger.debug("Updated user: {}", user.getLogin());
            } else {
                logger.debug("User login: {} is already up-to-date", user.getLogin());
            }
        } else {
            userRepository.save(user);
            logger.debug("New user saved: {}", user.getLogin());
        }
    }

    private boolean shouldUpdateUser(RedmineUser existing, RedmineUser newUser) {
        return !Objects.equals(existing.getFirstname(), newUser.getFirstname()) ||
               !Objects.equals(existing.getLastname(), newUser.getLastname()) ||
               !Objects.equals(existing.getMail(), newUser.getMail()) ||
               existing.isAdmin() != newUser.isAdmin();
    }

    private void updateUserFields(RedmineUser existing, RedmineUser newUser) {
        existing.setFirstname(newUser.getFirstname());
        existing.setLastname(newUser.getLastname());
        existing.setMail(newUser.getMail());
        existing.setAdmin(newUser.isAdmin());
    }

    private void handleUserException(RedmineUser user, Exception e) {
        String query = "Saving user with login: " + user.getLogin();
        logger.error("Database error occurred while processing user {}: {}", user.getLogin(), e.getMessage());
        logger.error("Failed Query: {}", query);
        logger.error("StackTrace: ", e);
        throw new DatabaseException("Error occurred while saving user", query, e);
    }

    // Deletion methods with improved error handling and batch processing
    public void deleteNonMatchingUsers(List<RedmineUser> users) {
        if (users == null) {
            logger.warn("Users list is null, skipping deletion");
            return;
        }

        try {
            List<RedmineUser> existingUsers = userRepository.findAll();
            List<String> userLogins = users.stream()
                .filter(user -> user.getLogin() != null && !user.getLogin().isEmpty())
                .map(RedmineUser::getLogin)
                .collect(Collectors.toList());

            List<RedmineUser> usersToDelete = existingUsers.stream()
                .filter(existingUser -> !userLogins.contains(existingUser.getLogin()))
                .collect(Collectors.toList());

            if (!usersToDelete.isEmpty()) {
                userRepository.deleteAll(usersToDelete);
                logger.info("Deleted {} users", usersToDelete.size());
                usersToDelete.forEach(user -> logger.debug("Deleted user: {}", user.getLogin()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting non-matching users: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while deleting non-matching users", "deleteNonMatchingUsers", e);
        }
    }

    public void deleteNonMatchingProjects(List<RedmineProject> projects) {
        if (projects == null) {
            logger.warn("Projects list is null, skipping deletion");
            return;
        }

        try {
            List<RedmineProject> existingProjects = projectRepository.findAll();
            List<String> projectIdentifiers = projects.stream()
                .filter(project -> project.getIdentifier() != null && !project.getIdentifier().isEmpty())
                .map(RedmineProject::getIdentifier)
                .collect(Collectors.toList());

            List<RedmineProject> projectsToDelete = existingProjects.stream()
                .filter(existingProject -> !projectIdentifiers.contains(existingProject.getIdentifier()))
                .collect(Collectors.toList());

            if (!projectsToDelete.isEmpty()) {
                projectRepository.deleteAll(projectsToDelete);
                logger.info("Deleted {} projects", projectsToDelete.size());
                projectsToDelete.forEach(project -> logger.debug("Deleted project: {}", project.getIdentifier()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting non-matching projects: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while deleting non-matching projects", "deleteNonMatchingProjects", e);
        }
    }

    public void deleteNonMatchingTrackers(List<RedmineTracker> trackers) {
        if (trackers == null) {
            logger.warn("Trackers list is null, skipping deletion");
            return;
        }

        try {
            List<RedmineTracker> existingTrackers = trackerRepository.findAll();
            List<Integer> trackerIds = trackers.stream()
                .map(RedmineTracker::getId)
                .collect(Collectors.toList());

            List<RedmineTracker> trackersToDelete = existingTrackers.stream()
                .filter(existingTracker -> !trackerIds.contains(existingTracker.getId()))
                .collect(Collectors.toList());

            if (!trackersToDelete.isEmpty()) {
                trackerRepository.deleteAll(trackersToDelete);
                logger.info("Deleted {} trackers", trackersToDelete.size());
                trackersToDelete.forEach(tracker -> logger.debug("Deleted tracker: {}", tracker.getName()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting non-matching trackers: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while deleting non-matching trackers", "deleteNonMatchingTrackers", e);
        }
    }

    public void deleteNonMatchingIssues(List<RedmineIssue> issues) {
        if (issues == null) {
            logger.warn("Issues list is null, skipping deletion");
            return;
        }

        try {
            List<RedmineIssue> existingIssues = issueRepository.findAll();
            List<Integer> issueIds = issues.stream()
                .map(RedmineIssue::getId)
                .collect(Collectors.toList());

            List<RedmineIssue> issuesToDelete = existingIssues.stream()
                .filter(existingIssue -> !issueIds.contains(existingIssue.getId()))
                .collect(Collectors.toList());

            if (!issuesToDelete.isEmpty()) {
                issueRepository.deleteAll(issuesToDelete);
                logger.info("Deleted {} issues", issuesToDelete.size());
                issuesToDelete.forEach(issue -> logger.debug("Deleted issue: {}", issue.getSubject()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting non-matching issues: {}", e.getMessage(), e);
            throw new DatabaseException("Error occurred while deleting non-matching issues", "deleteNonMatchingIssues", e);
        }
    }
}