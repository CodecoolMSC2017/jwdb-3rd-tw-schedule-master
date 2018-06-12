package com.codecool.web.dao;

import com.codecool.web.model.Day;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface DayDao {

    Day add(int scheduleId, String title) throws SQLException;

    void delete(int id) throws SQLException;

    void deleteByScheduleId(int scheduleId)throws SQLException;

    void update(int id, String newTitle) throws SQLException;

    Day findById(int id) throws SQLException;

    List<Day> findByScheduleId(int scheduleId) throws SQLException;

    Day findDayByTitle(String title) throws SQLException;

    void addDueDate(int dayId, Date dueDate) throws SQLException;

    void updateDueDate(int dayId, Date dueDate)throws SQLException;

    void deleteDueDateByDayId(int dayId)throws SQLException;

    Boolean isExists(int dayId)throws SQLException;

    Date getDateByDayId(int dayId)throws SQLException;

}
