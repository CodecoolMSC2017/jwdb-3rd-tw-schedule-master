package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;


public interface ScheduleDao {

    Schedule add(int appUserId, String title, String description) throws SQLException;

    void delete(int id) throws SQLException;

    void updateTitle(int id, String title) throws SQLException;

    void updateDescription(int id, String description) throws SQLException;

    Schedule findById(int id) throws SQLException;

    List<Schedule> findAllByUserId(int userId) throws SQLException;

    List<Schedule> findAll() throws SQLException;

    Schedule findByTitle(String title) throws SQLException;


}
