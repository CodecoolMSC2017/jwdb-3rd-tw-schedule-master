package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SimpleTaskService implements TaskService {

    private final TaskDao taskDao;

    public SimpleTaskService(Connection connection) throws SQLException {
        taskDao = (TaskDao) AbstractDaoFactory.getDao("task", connection);
    }

    @Override
    public void addTask(int userId, String title, String description) throws SQLException {
        taskDao.addTask(userId, title, description);
    }

    @Override
    public void deleteTask(int taskId) throws SQLException {
        taskDao.deleteTask(taskId);
    }

    @Override
    public void update(int taskId, String title, String description) throws SQLException {
        Task task = taskDao.findTaskById(taskId);
        String currentTitle = task.getTitle();
        String currentDescription = task.getDescription();

        if (currentTitle.equals(title) && !currentDescription.equals(description)) {
            taskDao.updateTaskDescription(taskId, description);
        } else if (!currentTitle.equals(title) && currentDescription.equals(description)) {
            taskDao.updateTaskTitle(taskId, title);
        } else if (!currentTitle.equals(title) && !currentDescription.equals(description)) {
            taskDao.updateTaskTitle(taskId, title);
            taskDao.updateTaskDescription(taskId, description);
        }
    }

    @Override
    public Task findById(int taskId) throws SQLException {
        return taskDao.findTaskById(taskId);
    }

    @Override
    public List<Task> findAll() throws SQLException {
        return taskDao.findAllTask();
    }

    @Override
    public List<Task> findAllByUserId(int userId) throws SQLException {
        return taskDao.findTaskByUserId(userId);
    }
}
