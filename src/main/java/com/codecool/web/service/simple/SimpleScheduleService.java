package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.model.Day;
import com.codecool.web.model.Hour;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;

import java.sql.SQLException;
import java.util.List;

public class SimpleScheduleService implements ScheduleService {

    ScheduleDao scheduleDao;

    public SimpleScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public void createSchedule(String title, String description, int userId) throws SQLException {

    }

    @Override
    public void deleteSchedule(int scheduleId) throws SQLException {

    }

    @Override
    public void updateSchedule(int scheduleId, String title, String description) throws SQLException {

    }

    @Override
    public Schedule findById(int scheduleId) throws SQLException {
        return null;
    }

    @Override
    public List<Schedule> findAll() throws SQLException {
        return null;
    }

    @Override
    public List<Schedule> findAllByUserId(int userId) throws SQLException {
        return null;
    }

    @Override
    public void addDay(int scheduleId, String title) throws SQLException {

    }

    @Override
    public void updateDay(int dayId, String title) throws SQLException {

    }

    @Override
    public Day findDayById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Day> findDayByScheduleId(int scheduleId) throws SQLException {
        return null;
    }

    @Override
    public Hour findHourById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Hour> findHoursByDayId(int dayId) throws SQLException {
        return null;
    }
}

