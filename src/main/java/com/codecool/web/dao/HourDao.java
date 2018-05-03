package com.codecool.web.dao;

import com.codecool.web.model.Hour;

import java.sql.SQLException;
import java.util.List;

public interface HourDao {

    void addHour(int day_id, int value) throws SQLException;

    void deleteHour(int id) throws SQLException;

    Hour findHourById(int id) throws SQLException;

    List<Hour> findHourByUserId(int userId) throws SQLException;
}
