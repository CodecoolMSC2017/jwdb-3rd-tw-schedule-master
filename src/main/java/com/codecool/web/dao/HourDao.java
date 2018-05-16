package com.codecool.web.dao;

import com.codecool.web.model.Hour;

import java.sql.SQLException;
import java.util.List;

public interface HourDao {

    void add(int dayId, int value) throws SQLException;

    void delete(int id) throws SQLException;

    void deleteByDayId(int dayId)throws SQLException;

    Hour findById(int id) throws SQLException;

    List<Hour> findByDayId(int dayId) throws SQLException;
}
