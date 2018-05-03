package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskHourDao;

import java.sql.Connection;

public class DatabaseTaskHourDao extends AbstractDao implements TaskHourDao {
    public DatabaseTaskHourDao(Connection connection) {
        super(connection);
    }
}
