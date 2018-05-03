package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.model.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseTaskDao extends AbstractDao implements TaskDao {
    public DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addTask(int userId, String title, String description) throws SQLException {

    }

    @Override
    public void deleteTask(int id) throws SQLException {

    }

    @Override
    public void updateTaskTitle(int id, String newTitle) throws SQLException {

    }

    @Override
    public void updateTaskDescription(int id, String newDescription) throws SQLException {

    }

    @Override
    public Task findTaskById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Task> findTaskByUserId(int userId) throws SQLException {
        return null;
    }

    @Override
    public List<Task> findAllTask() throws SQLException {
        return null;
    }
}
