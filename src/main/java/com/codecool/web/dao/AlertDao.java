package com.codecool.web.dao;

import java.sql.SQLException;
import java.util.List;

public interface AlertDao {
    List<Integer> getTodaysAlerts() throws SQLException;

    int getTaskIdByDayId(int dayId, String hourId)throws SQLException;

    List<Integer> getHourIdsByDayId(int dayId)throws SQLException;
}
