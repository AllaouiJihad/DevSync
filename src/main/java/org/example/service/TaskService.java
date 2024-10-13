package org.example.service;


import org.example.model.entities.Task;
import org.example.model.entities.User;
import org.example.model.entities.UserToken;
import org.example.model.enums.Status;
import org.example.repository.implementation.TaskRepositoryImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {

    TaskRepositoryImpl taskRepository;

    public TaskService() {
        this.taskRepository = new TaskRepositoryImpl();
    }
    private final UserTokenService userTokenService = new UserTokenService();


    public void createTask(Task task) {
        taskRepository.save(task);
        User assignedUser = task.getAssignedTo();

        UserToken replacementToken = userTokenService.findByUserAndTokenType(assignedUser, "Remplacement");
        if (replacementToken == null) {
            replacementToken = new UserToken();
            replacementToken.setUser(assignedUser);
            replacementToken.setTokenType("Remplacement");
            replacementToken.setTokenCount(2);
            replacementToken.setLastReset(LocalDate.now());
            userTokenService.save(replacementToken);
        }

        UserToken deletionToken = userTokenService.findByUserAndTokenType(assignedUser, "Suppression");
        if (deletionToken == null) {
            deletionToken = new UserToken();
            deletionToken.setUser(assignedUser);
            deletionToken.setTokenType("Suppression");
            deletionToken.setTokenCount(1);
            deletionToken.setLastReset(LocalDate.now());
            userTokenService.save(deletionToken);
        }
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAll();
    }

    public List<Task> getTasksByCreator(Long taskId) {
        return taskRepository.getTasksByCreatorId(taskId);
    }

    public List<Task> getTasksByAssigned(Long taskId) {

        return taskRepository.getTasksByAssigneeId(taskId);
    }

    public void changeStatus(long taskId, Status status) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(status);
            updateTask(task);
        }
    }

    public List<Task> findOverdueTasks(LocalDate date) {
        List<Task> overdueTasks = taskRepository.findOverdueTasks(date);
        for (Task task : overdueTasks) {
            task.setStatus(Status.NON_EFFECTUER);
            updateTask(task);
        }
        return overdueTasks;
    }

    public Map<String, Long> calculateStatisticsByCreatorId(Long creatorId) {
        List<Task> createdTasks = taskRepository.getTasksByCreatorId(creatorId);

        long totalCreatedTasks = createdTasks.size();
        long completedCreatedTasks = createdTasks.stream()
                .filter(task -> task.getStatus().equals(Status.TERMINEE))
                .count();
        long inProgressCreatedTasks = createdTasks.stream()
                .filter(task -> task.getStatus().equals(Status.EN_COURS))
                .count();
        long notStartedCreatedTasks = createdTasks.stream()
                .filter(task -> task.getStatus().equals(Status.A_FAIRE))
                .count();
        long notEffectuedCreatedTasks = createdTasks.stream()
                .filter(task -> task.getStatus().equals(Status.NON_EFFECTUER))
                .count();

        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalCreatedTasks", totalCreatedTasks);
        statistics.put("completedCreatedTasks", completedCreatedTasks);
        statistics.put("inProgressCreatedTasks", inProgressCreatedTasks);
        statistics.put("notStartedCreatedTasks", notStartedCreatedTasks);
        statistics.put("notEffectuedCreatedTasks", notEffectuedCreatedTasks);

        return statistics;
    }

}
