package org.example.service;

import org.example.model.entities.Task;
import org.example.model.entities.TaskHistory;
import org.example.model.entities.User;
import org.example.model.entities.UserToken;
import org.example.repository.implementation.TaskHistoryRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHistoryService {

    private final TaskHistoryRepositoryImpl taskHistoryRepository = new TaskHistoryRepositoryImpl();
    private final TaskService taskService = new TaskService();
    private final UserTokenService userTokenService = new UserTokenService();

    //    public void addTaskHistory(TaskHistory taskHistory){
//        taskHistoryDao.save(taskHistory);
//    }
    public void AskToRemplace(Task task, User user) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setUser(user);
        taskHistory.setIsApproved(false);
        taskHistory.setTypeModification("Remplacement");
        taskHistory.setModificationDate(LocalDate.now().atStartOfDay());
        taskHistoryRepository.save(taskHistory);
        String tokenType = "Remplacement";
        userToken(user, tokenType);
    }

    public void AskToRemove(Task task, User user) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setUser(user);
        taskHistory.setIsApproved(false);
        taskHistory.setTypeModification("Suppression");
        taskHistory.setModificationDate(LocalDate.now().atStartOfDay());
        taskHistoryRepository.save(taskHistory);
        String tokenType = "Suppression";
        userToken(user, tokenType);
    }

    private void userToken(User user, String tokenType) {
        UserToken existingUserToken = userTokenService.findByUserAndTokenType(user, tokenType);
        if (existingUserToken != null) {
            int currentCount = existingUserToken.getTokenCount();
            if (currentCount > 0) {
                existingUserToken.setTokenCount(currentCount - 1);
                existingUserToken.setLastReset(LocalDate.now());
                userTokenService.update(existingUserToken);
            } else {
                System.out.println("Le compteur de jetons est déjà à zéro pour cet utilisateur.");
            }
        } else {
            System.out.println("Aucun jeton trouvé pour cet utilisateur avec le type de jeton spécifié.");
        }
    }


    public void approveRemplace(TaskHistory taskHistory) {
        taskHistory.setIsApproved(true);
        taskHistoryRepository.update(taskHistory);
    }
    public void desapproveRemplace(TaskHistory taskHistory) {
        taskHistory.setIsApproved(false);
        taskHistoryRepository.update(taskHistory);

    }
    public void updateTaskHistory(TaskHistory taskHistory) {
        taskHistoryRepository.update(taskHistory);
    }

    public TaskHistory getTaskHistoryById(long id) {
        return taskHistoryRepository.findById(id).orElse(null);
    }

    public List<TaskHistory> getAllTaskHistory() {
        return taskHistoryRepository.getAllTaskHistory();
    }

    public List<TaskHistory> getMyRequestToApproved(User user) {
        List<TaskHistory> allTaskHistory = taskHistoryRepository.findAll();
        List<TaskHistory> tasksWrittenByUser = allTaskHistory.stream()
                .filter(taskHistory -> taskHistory.getTask().getCreatedBy().getId().equals(user.getId()))
                .collect(Collectors.toList());

        return tasksWrittenByUser;
    }
}
