package com.codecool.web.dao;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    void addTask(int userId, String title, String description) throws SQLException;

    void deleteTask(int id) throws SQLException;

    void updateTaskTitle(int id, String newTitle) throws SQLException;

    void updateTaskDescription(int id, String newDescription) throws SQLException;

    Task findTaskById(int id) throws SQLException;

    List<Task> findTaskByUserId(int userId) throws SQLException;

    List<Task> findAllTask() throws SQLException;
}
