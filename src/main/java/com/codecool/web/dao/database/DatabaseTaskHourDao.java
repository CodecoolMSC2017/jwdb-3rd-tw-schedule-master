package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskHourDao;

import java.sql.Connection;

class DatabaseTaskHourDao extends AbstractDaoFactory implements TaskHourDao {
    DatabaseTaskHourDao(Connection connection) {
        super(connection);
    }
}
