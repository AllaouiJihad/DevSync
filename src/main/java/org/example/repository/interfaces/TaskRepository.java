package org.example.repository.interfaces;

import org.example.model.entities.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long id);

    Task update(Task task);

    void delete(Task task);

    List<Task> getAll();

    List<Task> getTasksByAssigneeId(Long id);
    List<Task> getTasksByCreatorId(Long id);

    List<Task> findOverdueTasks(LocalDate date);
    List<Task> findByTagsAndDateRangeAndCreator(String tag, LocalDateTime startDate, LocalDateTime endDate, Long creatorId);

}


