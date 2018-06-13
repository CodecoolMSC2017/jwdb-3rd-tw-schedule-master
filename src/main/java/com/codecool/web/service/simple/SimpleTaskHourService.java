package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.exception.TaskOverlapException;
import com.codecool.web.service.TaskHourService;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleTaskHourService implements TaskHourService {

    private final TaskHourDao taskHourDao;

    public SimpleTaskHourService(Connection connection) {
        this.taskHourDao = (TaskHourDao) AbstractDaoFactory.getDao("taskHour", connection);
    }

    @Override
    public void disconnect(int taskId, int scheduleId, int dayId) throws SQLException {
        taskHourDao.delete(taskId, scheduleId,dayId );
    }


    @Override
    public void connectTaskToSchedule(int scheduleId, int taskId, int dayId, String hourId) throws SQLException {
        taskHourDao.add(taskId, scheduleId,dayId , hourId);
    }

    @Override
    public void handleTaskConnection(int userId, int dayId, int taskLength, int scheduleId, int taskId, String hourId) throws SQLException, TaskOverlapException, InvalidArgumentException {
        for (int i = 0; i < taskLength; i++) {
            String checkHourId = Integer.toString(Integer.parseInt(hourId) + i);
            if (!taskHourDao.validateHourIds(userId, dayId, checkHourId)) {
                throw new TaskOverlapException();
            }
        }
        String hourIds = hourId;
        for (int i = 1; i < taskLength; i++) {
            hourIds += "," + Integer.toString(Integer.parseInt(hourId) + i);
        }
        if (taskHourDao.isConnectionExists(taskId, scheduleId, dayId)) {
            taskHourDao.update(taskId, scheduleId,dayId , hourIds);
        } else if (!taskHourDao.isConnectionExists(taskId, scheduleId,dayId )) {
            connectTaskToSchedule(scheduleId, taskId, dayId, hourIds );
        }
    }


}
