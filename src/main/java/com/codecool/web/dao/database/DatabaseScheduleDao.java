package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {
    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addSchedule(int appUserId, String title, String description) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO schedule (app_user_id, title, description) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, appUserId);
            statement.setString(2, title);
            statement.setString(3, description);
            executeInsert(statement);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        String sql = "Delete from schedule\n" +
                " where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

    }

    @Override
    public void updateTitle(int id, String title) throws SQLException {
        String sql = "update schedule\n" +
                " set title = ?\n" +
                " where id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

    }

    @Override
    public void updateDescription(int id, String description) throws SQLException {
        String sql = "update schedule\n" +
                " set description = ?\n" +
                " where id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, description);
            statement.setInt(2, id);
            statement.executeUpdate();
        }

    }

    @Override
    public Schedule findById(int id) throws SQLException {
        String sql = "select * from schedule\n" +
                " where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchSchedule(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<Schedule> findAllByUserId(int id) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "select * from schedule\n" +
                " where app_user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedules.add(fetchSchedule(resultSet));
                }
            }
        }
        return schedules;
    }

    @Override
    public List<Schedule> findall() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "Select * from schedule";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                schedules.add(fetchSchedule(resultSet));
            }
        }
        return schedules;
    }

    private Schedule fetchSchedule(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userID = resultSet.getInt("app_user_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        return new Schedule(id, userID, title, description);
    }
}
