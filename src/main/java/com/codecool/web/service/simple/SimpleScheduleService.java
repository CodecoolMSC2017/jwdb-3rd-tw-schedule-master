package com.codecool.web.service.simple;

import com.codecool.web.dao.DayDao;
import com.codecool.web.dao.HourDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.TooManyDaysException;
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
    private final TaskHourDao taskHourDao;

    public SimpleScheduleService(Connection connection) {
        scheduleDao = (ScheduleDao) AbstractDaoFactory.getDao("schedule", connection);
        dayDao = (DayDao) AbstractDaoFactory.getDao("day", connection);
        hourDao = (HourDao) AbstractDaoFactory.getDao("hour", connection);
        taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour",connection);
    }

    @Override
    public void createSchedule(String title, String description, int userId, int numOfDays) throws SQLException, TooManyDaysException {
        if(numOfDays > 7 || numOfDays == 0){
            throw new TooManyDaysException();
        }
        Schedule schedule = scheduleDao.add(userId, title, description);
        for (int i = 0; i < numOfDays; i++) {
            Day day = addDay(schedule.getId(), "Title");
            for (int j = 0; j < 24; j++) {
                hourDao.add(day.getId(), j);
            }
        }
    }

    @Override
    public void deleteSchedule(int scheduleId) throws SQLException {
        for (Day day : findDayByScheduleId(scheduleId)) {
            hourDao.deleteByDayId(day.getId());
        }
        dayDao.deleteByScheduleId(scheduleId);
        taskHourDao.deleteByScheduleId(scheduleId);
        scheduleDao.delete(scheduleId);
    }

    @Override
    public void updateSchedule(int scheduleId, String title, String description) throws SQLException {
        Schedule schedule = scheduleDao.findById(scheduleId);
        String scheduleTile = schedule.getTitle();
        String scheduleDescription = schedule.getDescription();
        if (scheduleTile.equals(title) && !scheduleDescription.equals(description)) {
            scheduleDao.updateDescription(scheduleId, description);
        } else if (!scheduleTile.equals(title) && scheduleDescription.equals(description)) {
            scheduleDao.updateTitle(scheduleId, title);
        } else if (!scheduleTile.equals(title) && !scheduleDescription.equals(description)) {
            scheduleDao.updateTitle(scheduleId, title);
            scheduleDao.updateDescription(scheduleId, description);
        }
    }

    @Override
    public Schedule findById(int scheduleId) throws SQLException {
        Schedule schedule = scheduleDao.findById(scheduleId);
        List<Day> days = dayDao.findByScheduleId(scheduleId);
        for (Day day : days) {
            day.setHours(hourDao.findByDayId(day.getId()));
        }
        schedule.setDays(days);
        return schedule;
    }

    @Override
    public List<Schedule> findAll() throws SQLException {
        List<Schedule> schedules = scheduleDao.findAll();
        schedules = getSchedules(schedules);
        return schedules;
    }

    @Override
    public List<Schedule> findAllByUserId(int userId) throws SQLException {
        List<Schedule> schedules = scheduleDao.findAllByUserId(userId);
        schedules = getSchedules(schedules);
        return schedules;
    }

    private List<Schedule> getSchedules(List<Schedule> schedules) throws SQLException {
        for (Schedule schedule : schedules) {
            List<Day> days = dayDao.findByScheduleId(schedule.getId());
            for (Day day : days) {
                day.setHours(hourDao.findByDayId(day.getId()));
            }
            schedule.setDays(days);
        }
        return schedules;
    }

    @Override
    public Day addDay(int scheduleId, String title) throws SQLException {
        return dayDao.add(scheduleId, title);
    }

    @Override
    public void updateDay(int dayId, String title) throws SQLException {
        dayDao.update(dayId, title);
    }

    @Override
    public Day findDayById(int id) throws SQLException {
        return dayDao.findById(id);
    }

    @Override
    public List<Day> findDayByScheduleId(int scheduleId) throws SQLException {
        return dayDao.findByScheduleId(scheduleId);
    }

    @Override
    public Hour findHourById(int id) throws SQLException {
        return hourDao.findById(id);
    }

    @Override
    public List<Hour> findHoursByDayId(int dayId) throws SQLException {
        return hourDao.findByDayId(dayId);
    }
}

