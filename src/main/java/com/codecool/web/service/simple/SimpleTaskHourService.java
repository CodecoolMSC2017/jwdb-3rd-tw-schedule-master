package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.service.TaskHourService;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleTaskHourService implements TaskHourService{

    private final TaskHourDao taskHourDao;

    public SimpleTaskHourService(Connection connection) {
        this.taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour",connection);
    }

    @Override
    public void disconnectTask(int taskId) throws SQLException {
        taskHourDao.deleteByTaskId(taskId);
    }

    @Override
    public void disconnectSchedule(int scheduleId) throws SQLException {
        taskHourDao.deleteByScheduleId(scheduleId);
    }

    @Override
    public void updateHours(int scheduleId, int taskId,String... newHourIds) throws SQLException, InvalidArgumentException {
        taskHourDao.update(taskId,scheduleId,newHourIds);
    }

}
