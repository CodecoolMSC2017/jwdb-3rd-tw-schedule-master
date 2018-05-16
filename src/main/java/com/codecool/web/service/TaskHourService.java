package com.codecool.web.service;

import com.codecool.web.exception.InvalidArgumentException;

import java.sql.SQLException;

public interface TaskHourService {

    void disconnectTask(int taskId)throws SQLException;

    void disconnectSchedule(int scheduleId)throws SQLException;

    void updateHours(int scheduleId, int taskId, String... newHourIds) throws SQLException, InvalidArgumentException;
}
