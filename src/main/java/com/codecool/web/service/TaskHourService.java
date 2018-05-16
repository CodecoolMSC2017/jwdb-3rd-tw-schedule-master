package com.codecool.web.service;

import java.sql.SQLException;

public interface TaskHourService {

    void disconnectTask(int taskId)throws SQLException;

    void disconnectSchedule(int scheduleId)throws SQLException;

    void updateHours(int scheduleId,int taskId)throws SQLException;
}
