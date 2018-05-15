package com.codecool.web.service.simple;

import com.codecool.web.dao.DayDao;
import com.codecool.web.dao.HourDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.model.Day;
import com.codecool.web.model.Hour;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SimpleScheduleService implements ScheduleService {

    private final ScheduleDao scheduleDao;
    private final DayDao dayDao;
    private final HourDao hourDao;

    public SimpleScheduleService(Connection connection) {
        scheduleDao = (ScheduleDao) AbstractDaoFactory.getDao("schedule", connection);
        dayDao = (DayDao) AbstractDaoFactory.getDao("day", connection);
        hourDao = (HourDao) AbstractDaoFactory.getDao("hour", connection);
    }

    @Override
    public void createSchedule(String title, String description, int userId,int numOfDays) throws SQLException {
        Schedule schedule = scheduleDao.addSchedule(userId,title, description);
        for(int i = 0; i< numOfDays;i++){
            Day day = addDay(schedule.getId(),"Title");
            for (int j = 0; j < 24; j++) {
                hourDao.addHour(day.getId(),j);
            }
        }

    }

    @Override
    public void deleteSchedule(int scheduleId) throws SQLException {
        scheduleDao.deleteSchedule(scheduleId);
    }

    @Override
    public void updateSchedule(int scheduleId, String title, String description) throws SQLException {
        Schedule schedule = scheduleDao.findById(scheduleId);
        String scheduleTile = schedule.getTitle();
        String scheduleDescription = schedule.getDescription();
        if(scheduleTile.equals(title) && !scheduleDescription.equals(description)){
            scheduleDao.updateDescription(scheduleId,description);
        }
        else if(!scheduleTile.equals(title) && scheduleDescription.equals(description)){
            scheduleDao.updateTitle(scheduleId, title);
        }
        else if(!scheduleTile.equals(title) && !scheduleDescription.equals(description)){
            scheduleDao.updateTitle(scheduleId, title);
            scheduleDao.updateDescription(scheduleId,description);
        }
    }

    @Override
    public Schedule findById(int scheduleId) throws SQLException {
        return scheduleDao.findById(scheduleId);
    }

    @Override
    public List<Schedule> findAll() throws SQLException {
        return scheduleDao.findall();
    }

    @Override
    public List<Schedule> findAllByUserId(int userId) throws SQLException {
        return scheduleDao.findAllByUserId(userId);
    }

    @Override
    public Day addDay(int scheduleId, String title) throws SQLException {
        return dayDao.addDay(scheduleId,title);
    }

    @Override
    public void updateDay(int dayId, String title) throws SQLException {
        dayDao.updateDay(dayId,title);
    }

    @Override
    public Day findDayById(int id) throws SQLException {
        return dayDao.findDayById(id);
    }

    @Override
    public List<Day> findDayByScheduleId(int scheduleId) throws SQLException {
        return dayDao.findDayByScheduleId(scheduleId);
    }

    @Override
    public Hour findHourById(int id) throws SQLException {
        return hourDao.findHourById(id);
    }

    @Override
    public List<Hour> findHoursByDayId(int dayId) throws SQLException {
        return hourDao.findHoursByDayId(dayId);
    }
}

