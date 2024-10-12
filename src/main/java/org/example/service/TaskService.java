package org.example.service;

import org.example.model.entities.Task;
import org.example.repository.implementation.TaskRepositoryImpl;
import org.example.repository.interfaces.TaskRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class TaskService {
    @Inject
    TaskRepositoryImpl taskRepository;



    public Task save(Task task){
        if (task.equals(new Task())){
            return null;
        }
        return taskRepository.save(task);



    }
}
