package com.codecool.web.service;

import com.codecool.web.exception.InvalidArgumentException;

import java.sql.SQLException;

public interface TaskHourService {

    void disconnect(String disconnectType,int id) throws SQLException;

    void updateHours(int scheduleId, int taskId, String... newHourIds) throws SQLException, InvalidArgumentException;

    void connectTaskToSchedule(int scheduleId,int taskId,String... hourIds) throws SQLException, InvalidArgumentException;
}
