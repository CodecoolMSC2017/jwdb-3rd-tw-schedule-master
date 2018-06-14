package com.codecool.web.dao.database;

import com.codecool.web.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class AbstractDaoFactory {

    private static AbstractDaoFactory dayDatabaseDao;
    private static AbstractDaoFactory hourDatabaseDao;
    private static AbstractDaoFactory scheduleDatabaseDao;
    private static AbstractDaoFactory taskDatabaseDao;
    private static AbstractDaoFactory taskHourDatabaseDao;
    private static AbstractDaoFactory userDatabaseDao;
    private static AbstractDaoFactory alertDatabaseDao;

    Connection connection;

    AbstractDaoFactory(Connection connection) {
        this.connection = connection;
    }

    public static AbstractDaoFactory getDao(String daoType, Connection connection) {
        AbstractDaoFactory dao;
        switch (daoType) {
            case "day":
                if (dayDatabaseDao == null) {
                    dayDatabaseDao = new DatabaseDayDao(connection);
                }
                dao = dayDatabaseDao;
                break;
            case "hour":
                if (hourDatabaseDao == null) {
                    hourDatabaseDao = new DatabaseHourDao(connection);
                }
                dao = hourDatabaseDao;
                break;
            case "schedule":
                if (scheduleDatabaseDao == null) {
                    scheduleDatabaseDao = new DatabaseScheduleDao(connection);
                }
                dao = scheduleDatabaseDao;
                break;
            case "task":
                if (taskDatabaseDao == null) {
                    taskDatabaseDao = new DatabaseTaskDao(connection);
                }
                dao = taskDatabaseDao;
                break;
            case "taskHour":
                if (taskHourDatabaseDao == null) {
                    taskHourDatabaseDao = new DatabaseTaskHourDao(connection);
                }
                dao = taskHourDatabaseDao;
                break;
            case "user":
                if (userDatabaseDao == null) {
                    userDatabaseDao = new DatabaseUserDao(connection);
                }
                dao = userDatabaseDao;
                break;
            case "alert":
                if (alertDatabaseDao == null) {
                    alertDatabaseDao = new DatabaseAlertDao(connection);
                }
                dao = alertDatabaseDao;
                break;
            default:
                return null;
        }
        dao.setConnection(connection);
        return dao;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    void executeInsert(PreparedStatement statement) throws SQLException {
        int insertCount = statement.executeUpdate();
        if (insertCount != 1) {
            connection.rollback();
            throw new SQLException("Expected 1 row to be inserted");
        }
    }

    void update(int id, String updatedData, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedData);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    void delete(int id, String sql) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
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

    int fetchGeneratedId(PreparedStatement statement) throws SQLException {
        int id;
        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                connection.rollback();
                throw new SQLException("Expected 1 result");
            }
        }
        connection.commit();
        return id;
    }

    public Task fetchTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("app_user_id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String color = resultSet.getString("color");
        return new Task(id, userId, title, description, color);
    }
}
