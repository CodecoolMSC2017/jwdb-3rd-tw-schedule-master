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
    public void disconnect(int id) throws SQLException {
        taskHourDao.deleteByTaskId(id);
    }

    @Override
    public void updateHours(int scheduleId, int taskId, String... newHourIds) throws SQLException, InvalidArgumentException {
        taskHourDao.update(taskId, scheduleId, newHourIds);
    }

    @Override
    public void connectTaskToSchedule(int scheduleId, int taskId, String hourId) throws SQLException, InvalidArgumentException {
        taskHourDao.add(taskId, scheduleId, hourId);
    }

    @Override
    public void handleTaskConnection(int userId,int dayId,int taskLength,int scheduleId,int taskId,String hourId) throws SQLException, TaskOverlapException, InvalidArgumentException {
        for (int i = 1; i < taskLength; i++) {
            String checkHourId = Integer.toString(Integer.parseInt(hourId)+i);
            if(!taskHourDao.validateHourIds(userId,dayId,checkHourId)){
                throw new TaskOverlapException();
            }
        }
        String hourIds = hourId;
        for (int i = 1; i < taskLength; i++) {
             hourIds += ","+Integer.toString(Integer.parseInt(hourId)+i);
        }
        connectTaskToSchedule(scheduleId,taskId,hourIds);
    }




}
