package org.example.repository.interfaces;

import org.example.model.entities.TaskHistory;

import java.util.List;
import java.util.Optional;

public interface TaskHistoryRepository {
    TaskHistory save(TaskHistory taskHistory);
    Optional<TaskHistory> findById(Long id);
    List<TaskHistory> findAll();
    TaskHistory update(TaskHistory taskHistory);

    List<TaskHistory> getAllTaskHistory();
}
