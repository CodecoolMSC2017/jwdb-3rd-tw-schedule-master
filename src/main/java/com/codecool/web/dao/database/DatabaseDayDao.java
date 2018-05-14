package com.codecool.web.dao.database;

import com.codecool.web.dao.DayDao;
import com.codecool.web.model.Day;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class DatabaseDayDao extends AbstractDaoFactory implements DayDao {
   DatabaseDayDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addDay(int scheduleId, String title) throws SQLException {

    }

    @Override
    public void deleteDay(int id) throws SQLException {

    }

    @Override
    public void updateDay(int id, String newTitle) throws SQLException {

    }

    @Override
    public Day findDayById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Day> findDayByScheduleId(int scheduleId) throws SQLException {
        return null;
    }
}
