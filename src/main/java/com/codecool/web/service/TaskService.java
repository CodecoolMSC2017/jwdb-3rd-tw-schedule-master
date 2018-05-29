package com.codecool.web.service;

import com.codecool.web.exception.TaskAlreadyExistsException;
import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskService {

    void addTask(int userId, String title, String description, String color) throws SQLException;

    void deleteTask(Task task) throws SQLException;

    void update(Task task) throws SQLException, TaskAlreadyExistsException;

    Task findById(int taskId) throws SQLException;

    List<Task> findAll() throws SQLException;

    List<Task> findAllByUserId(int userId) throws SQLException;
}
