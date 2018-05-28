package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DatabaseScheduleDao extends AbstractDaoFactory implements ScheduleDao {
    DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public Schedule add(int appUserId, String title, String description) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedule (app_user_id, title, description) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, appUserId);
            statement.setString(2, title);
            statement.setString(3, description);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            return new Schedule(id, appUserId, title, description);
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM schedule\n" +
                " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void updateTitle(int id, String title) throws SQLException {
        String sql = "UPDATE schedule\n" +
                " SET title = ?\n" +
                " WHERE id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

    }

    @Override
    public void updateDescription(int id, String description) throws SQLException {
        String sql = "UPDATE schedule\n" +
                " SET description = ?\n" +
                " WHERE id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, description);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

    }

    @Override
    public Schedule findById(int id) throws SQLException {
        String sql = "SELECT * FROM schedule\n" +
                " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetch(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<Schedule> findAllByUserId(int id) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule\n" +
                " WHERE app_user_id = ? ORDER BY title ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedules.add(fetch(resultSet));
                }
            }
        }
        return schedules;
    }

    @Override
    public List<Schedule> findAll() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule ORDER BY title ASC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                schedules.add(fetch(resultSet));
            }
        }
        return schedules;
    }

    @Override
    public Schedule findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM schedule WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return fetch(resultSet);
            }

        }return null;
    }



    private Schedule fetch(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userID = resultSet.getInt("app_user_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        return new Schedule(id, userID, title, description);
    }
}
