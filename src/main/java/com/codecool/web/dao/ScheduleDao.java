package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ScheduleDao {

    void insertSchedule(int app_user_id, String title, String description) throws SQLException;

    void deleteSchedule(int id) throws SQLException;

    void updateTitle(int id, String title) throws SQLException;

    void updateDescription(int id, String description) throws SQLException;

    Schedule findById(int id) throws SQLException;

    ArrayList<Schedule> findAllByUserId(int id) throws SQLException;

    ArrayList<Schedule> findall() throws SQLException;


}
