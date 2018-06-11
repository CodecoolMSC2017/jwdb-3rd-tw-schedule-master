package com.codecool.web.service;

import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.exception.TaskOverlapException;

import java.sql.SQLException;

public interface TaskHourService {

    void disconnect(int taskId, int scheduleId) throws SQLException;

    void connectTaskToSchedule(int scheduleId, int taskId, String hourId) throws SQLException;

    void handleTaskConnection(int userId,int dayId,int taskLength,int scheduleId,int taskId,String hourId) throws SQLException, TaskOverlapException, InvalidArgumentException;
}
