package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseTaskDao extends AbstractDaoFactory implements TaskDao {

    DatabaseTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addTask(int userId, String title, String description) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO task (app_user_id, title, description)\n" +
                "\tVALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, description);
            executeInsert(statement);
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteTask(int id) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "DELETE FROM task WHERE task.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateTaskTitle(int id, String newTitle) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE task SET title = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newTitle);
            statement.setInt(2, id);
            statement.executeUpdate();
        }  catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void updateTaskDescription(int id, String newDescription) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "UPDATE task SET description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newDescription);
            statement.setInt(2, id);
            statement.executeUpdate();
        }  catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public Task findTaskById(int id) throws SQLException {
        String sql = "SELECT id, app_user_id, title, description FROM task WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return fetchTask(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<Task> findTaskByUserId(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, app_user_id, title, description FROM task WHERE app_user_id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(fetchTask(resultSet));
                }
            }
        }
        return tasks;
    }

    @Override
    public List<Task> findAllTask() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, app_user_id, title, description FROM task";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(fetchTask(resultSet));
                }
            }
        }
        return tasks;
    }

    private Task fetchTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("app_user_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        return new Task(id, userId, title, description);
    }
}
