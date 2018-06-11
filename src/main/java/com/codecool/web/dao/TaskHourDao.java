package com.codecool.web.dao;

import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskHourDao {

    void add(int taskId, int scheduleId, String hourId) throws SQLException;

    void delete(int taskId,int scheduleId)throws SQLException;

    void deleteByTaskId(int taskId)throws SQLException;

    void deleteByScheduleId(int scheduleId)throws SQLException;

    void update(int taskId, int scheduleId, String hourId) throws SQLException, InvalidArgumentException;

    List<Task> findTaskByScheduleId(int scheduleId)throws SQLException;

    List<String> findHoursByTaskAndScheduleId(int taskId, int scheduleId)throws SQLException;

    Boolean validateHourIds(int userId,int dayId, String hourId) throws SQLException;

    Boolean isConnectionExists(int taskId, int scheduleId) throws SQLException;



}
