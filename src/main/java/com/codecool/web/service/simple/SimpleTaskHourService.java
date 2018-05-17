package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.service.TaskHourService;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleTaskHourService implements TaskHourService {

    private final TaskHourDao taskHourDao;

    public SimpleTaskHourService(Connection connection) {
        this.taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour", connection);
    }

    @Override
    public void disconnect(String disconnectType, int id) throws SQLException {
        switch (disconnectType) {
            case "task":
                taskHourDao.deleteByTaskId(id);
                break;
            case "schedule":
                taskHourDao.deleteByScheduleId(id);
                break;
            default:
                throw new IllegalArgumentException();

        }

    }

    @Override
    public void updateHours(int scheduleId, int taskId, String... newHourIds) throws SQLException, InvalidArgumentException {
        taskHourDao.update(taskId, scheduleId, newHourIds);
    }

    @Override
    public void connectTaskToSchedule(int scheduleId, int taskId, String... hourIds) throws SQLException, InvalidArgumentException {
        taskHourDao.add(taskId, scheduleId, hourIds);
    }


}
