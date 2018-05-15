package com.codecool.web.dao;

import com.codecool.web.model.Day;

import java.sql.SQLException;
import java.util.List;

public interface DayDao {

    Day addDay(int scheduleId, String title) throws SQLException;

    void deleteDay(int id) throws SQLException;

    void updateDay(int id, String newTitle) throws SQLException;

    Day findDayById(int id) throws SQLException;

    List<Day> findDayByScheduleId(int scheduleId) throws SQLException;
}
