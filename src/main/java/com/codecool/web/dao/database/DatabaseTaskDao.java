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
    public void add(int userId, String title, String description, String color) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO task (app_user_id, title, description, color)\n" +
                "\tVALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, color);
            executeInsert(statement);
        }finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM task WHERE task.id = ?";
        delete(id, sql);
    }

    @Override
    public void updateTitle(int id, String newTitle) throws SQLException {
        String sql = "UPDATE task SET title = ? WHERE id = ?";
        update(id, newTitle, sql);
    }

    @Override
    public void updateDescription(int id, String newDescription) throws SQLException {
        String sql = "UPDATE task SET description = ? WHERE id = ?";
        update(id, newDescription, sql);
    }

    @Override
    public void updateColor(int taskId, String newColor) throws SQLException {
        String sql = "UPDATE task SET color = ? WHERE id = ?";
        update(taskId, newColor, sql);
    }

    @Override
    public Task findById(int id) throws SQLException {
        String sql = "SELECT id, app_user_id, title, description, color FROM task WHERE id = ?";
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
    public List<Task> findByUserId(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE app_user_id = ? ";
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


    public List<Task> findByUserIdAndScheduleId(int userId, int scheduleId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id,app_user_id,title,description,color,array_agg(schedule_id)\n" +
                "                FROM task\n" +
                "                LEFT JOIN task_hour ON task_id = id\n" +
                "                WHERE app_user_id = ?\n" +
                "                AND id not in (SELECT id\n" +
                "               FROM task\n" +
                "                LEFT JOIN task_hour ON task_id = id\n" +
                "                WHERE schedule_id = ?)\n" +
                "                AND (schedule_id <> ?\n" +
                "                OR schedule_id IS null)\n" +
                "                GROUP BY id\n" +
                "                ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(2, scheduleId);
            statement.setInt(3, scheduleId);
            statement.setInt(1,userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(fetchTask(resultSet));
                }
            }
            return tasks;
        }

    }



    @Override
    public List<Task> findAll() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task.id, app_user_id, title, description, color FROM task ORDER BY title ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(fetchTask(resultSet));
                }
            }
        }
        return tasks;
    }

    @Override
    public Task findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM task WHERE title = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,title);
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){

                    return fetchTask(resultSet);
                }
            }
        }
        return null;

    }
}
