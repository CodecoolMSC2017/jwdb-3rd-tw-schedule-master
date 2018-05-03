package com.codecool.web.service;

import com.codecool.web.model.Task;

import java.util.List;

public interface TaskService {

    void createTask(int userId, String title, String description);

    void deleteTask(int taskId);

    void update(int taskId, String title, String description);

    Task findById(int taskId);

    List<Task> findAll();

    List<Task> findAllByUserId(int userId);
}
