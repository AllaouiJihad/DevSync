package org.example.service;


import org.example.repository.implementation.TaskRepositoryImpl;

public class TaskService {

    TaskRepositoryImpl taskRepository;

    public TaskService(TaskRepositoryImpl taskRepository) {
        this.taskRepository = taskRepository;
    }


}
