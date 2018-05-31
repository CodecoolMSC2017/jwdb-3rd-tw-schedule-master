package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.TaskAlreadyExistsException;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SimpleTaskService implements TaskService {

    private final TaskDao taskDao;
    private final TaskHourDao taskHourDao;

    public SimpleTaskService(Connection connection) {
        taskDao = (TaskDao) AbstractDaoFactory.getDao("task", connection);
        taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour",connection);

    }

    @Override
    public void addTask(int userId, String title, String description, String color) throws SQLException, TaskAlreadyExistsException {
        Task check = taskDao.findByTitle(title);
        if(check != null && check.getUserId() == userId && check.getTitle().equals(title)){
            throw new TaskAlreadyExistsException();
        }
        taskDao.add(userId, title, description, color);
    }

    @Override
    public void deleteTask(Task task) throws SQLException {
        int taskId = task.getId();
        taskHourDao.deleteByTaskId(taskId);
        taskDao.delete(taskId);

    }

    @Override
    public void update(Task task) throws SQLException, TaskAlreadyExistsException {
        int taskId = task.getId();
        String title = task.getTitle();
        String description = task.getDescription();
        String color = task.getColor();

        Task taskToUpdate = taskDao.findById(taskId);
        Task check = taskDao.findByTitle(title);
        String currentTitle = taskToUpdate.getTitle();
        String currentDescription = taskToUpdate.getDescription();
        String currentColor = taskToUpdate.getColor();
        if(check == null || check.getId() == taskId)  {
            check = null;
        }
        if (currentTitle.equals(title) && !currentDescription.equals(description) && currentColor.equals(color)) {
            taskDao.updateDescription(taskId, description);
        } else if (!currentTitle.equals(title) && currentDescription.equals(description) && currentColor.equals(color) && check == null) {
            taskDao.updateTitle(taskId, title);
        } else if (!currentTitle.equals(title) && !currentDescription.equals(description) && currentColor.equals(color) && check == null) {
            taskDao.updateTitle(taskId, title);
            taskDao.updateDescription(taskId, description);
        } else if (!currentTitle.equals(title) && !currentDescription.equals(description) && !currentColor.equals(color) && check == null) {
            taskDao.updateTitle(taskId, title);
            taskDao.updateDescription(taskId, description);
            taskDao.updateColor(taskId, color);
        } else if (currentTitle.equals(title) && currentDescription.equals(description) && !currentColor.equals(color)) {
            taskDao.updateColor(taskId, color);
        }
        else if(check != null){
            throw new TaskAlreadyExistsException();
        }
    }

    @Override
    public Task findById(int taskId) throws SQLException {
        return taskDao.findById(taskId);
    }

    @Override
    public List<Task> findAll() throws SQLException {
        return taskDao.findAll();
    }

    @Override
    public List<Task> findAllByUserId(int userId) throws SQLException {
        return taskDao.findByUserId(userId);
    }

    @Override
    public List<Task> findAllByUserAndScheduleId(int userId, int scheduleId) throws SQLException {
        return taskDao.findByUserIdAndScheduleId(userId,scheduleId);
    }
}
