package com.codecool.web.dao.database;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Schedule;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseScheduleDao extends AbstractDao implements ScheduleDao {
    public DatabaseScheduleDao(Connection connection) {
        super(connection);
    }

    @Override
    public void insertSchedule(int app_user_id, String title, String description) throws SQLException {

    }

    @Override
    public void deleteSchedule(int id) throws SQLException {

    }

    @Override
    public void updateTitle(int id, String title) throws SQLException {

    }

    @Override
    public void updateDescription(int id, String description) throws SQLException {

    }

    @Override
    public Schedule findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Schedule> findAllByUserId(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Schedule> findall() throws SQLException {
        return null;
    }
}
