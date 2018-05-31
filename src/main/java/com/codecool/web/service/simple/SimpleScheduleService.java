package com.codecool.web.service.simple;

import com.codecool.web.dao.DayDao;
import com.codecool.web.dao.HourDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.DayAlreadyExistsException;
import com.codecool.web.exception.ScheduleAlreadyExistsException;
import com.codecool.web.exception.WrongNumOfDaysException;
import com.codecool.web.model.Day;
import com.codecool.web.model.Hour;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
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
        taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour", connection);
    }

    @Override
    public void createSchedule(String title, String description, int userId, int numOfDays) throws SQLException, WrongNumOfDaysException, ScheduleAlreadyExistsException, DayAlreadyExistsException {
        Schedule check = scheduleDao.findByTitle(title);
        if (numOfDays > 7 || numOfDays == 0) {
            throw new WrongNumOfDaysException();
        }
        if(check != null && check.getUserId() == userId){
            throw new ScheduleAlreadyExistsException();
        }
        Schedule schedule = scheduleDao.add(userId, title, description);
        for (int i = 0; i < numOfDays; i++) {
            Day day = addDay(schedule.getId(), String.format("Day%d", i + 1), userId);
            for (int j = 0; j < 24; j++) {
                hourDao.add(day.getId(), j);
            }
        }
    }

    @Override
    public void deleteSchedule(Schedule schedule) throws SQLException {
        int scheduleId = schedule.getId();
        for (Day day : findDayByScheduleId(scheduleId)) {
            hourDao.deleteByDayId(day.getId());
        }
        dayDao.deleteByScheduleId(scheduleId);
        taskHourDao.deleteByScheduleId(scheduleId);
        scheduleDao.delete(scheduleId);
    }

    @Override
    public void updateSchedule(Schedule schedule) throws SQLException, ScheduleAlreadyExistsException {
        int scheduleId = schedule.getId();
        String title = schedule.getTitle();
        String description = schedule.getDescription();

        Schedule scheduleToUpdate = scheduleDao.findById(scheduleId);
        Schedule check = scheduleDao.findByTitle(title);
        String currentTitle = scheduleToUpdate.getTitle();
        String currentDescription = scheduleToUpdate.getDescription();
        if (currentTitle.equals(title) && !currentDescription.equals(description)) {
            scheduleDao.updateDescription(scheduleId, description);
        } else if (!currentTitle.equals(title) && currentDescription.equals(description) && check == null) {
            scheduleDao.updateTitle(scheduleId, title);
        } else if (!currentTitle.equals(title) && !currentDescription.equals(description) && check == null) {
            scheduleDao.updateTitle(scheduleId, title);
            scheduleDao.updateDescription(scheduleId, description);
        }else if(check != null){
            throw new ScheduleAlreadyExistsException();
        }
    }

    @Override
    public Schedule findById(int scheduleId) throws SQLException {
        Schedule schedule = scheduleDao.findById(scheduleId);
        List<Task> tasks = taskHourDao.findTaskByScheduleId(schedule.getId());
        List<Day> days = dayDao.findByScheduleId(scheduleId);
        for (Day day : days) {
            List<Hour> hours = hourDao.findByDayId(day.getId());
            for (Hour hour : hours) {
                for (Task task : tasks) {
                    List<String> hourIds = taskHourDao.findHoursByTaskAndScheduleId(task.getId(), schedule.getId());
                    if (hour.getDayId() == day.getId() && hourIds.contains(Integer.toString(hour.getId()))) {
                        hour.setTask(task);
                    }
                }

            }
            day.setHours(hours);
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
    public Day addDay(int scheduleId, String title, int userId) throws SQLException, DayAlreadyExistsException {
        Day day = dayDao.findDayByTitle(title);

        if (day != null) {
            Schedule schedule = scheduleDao.findById(day.getScheduleId());
            if (schedule.getUserId() == userId && schedule.getId() == scheduleId){
                throw new DayAlreadyExistsException();
            }
        }
        return dayDao.add(scheduleId, title);
    }

    @Override
    public void updateDay(int dayId, String title, int userId) throws SQLException, DayAlreadyExistsException {
        Day day = dayDao.findDayByTitle(title);


        if (day != null) {
            Schedule schedule = scheduleDao.findById(day.getScheduleId());
            if ( schedule.getUserId() == userId){
                throw new DayAlreadyExistsException();
            }
        }
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

