package com.codecool.web.dao.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public abstract class AbstractDaoFactory {

    private static AbstractDaoFactory dayDatabaseDao;
    private static AbstractDaoFactory hourDatabaseDao;
    private static AbstractDaoFactory scheduleDatabaseDao;
    private static AbstractDaoFactory taskDatabaseDao;
    private static AbstractDaoFactory taskHourDatabaseDao;
    private static AbstractDaoFactory userDatabaseDao;

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
}
