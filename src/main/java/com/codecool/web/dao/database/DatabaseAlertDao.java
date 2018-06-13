package com.codecool.web.dao.database;

import com.codecool.web.dao.AlertDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseAlertDao extends AbstractDaoFactory implements AlertDao{
    DatabaseAlertDao(Connection connection){super(connection);}

    @Override
    public List<Integer> getTodaysAlerts() throws SQLException {
        List<Integer> dayIds = new ArrayList<>();
        Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        String sql = "SELECT day_id FROM alert WHERE due_date = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1,sqlDate);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    dayIds.add(resultSet.getInt("day_id"));
                }
                return dayIds;
            }
        }
    }

    @Override
    public List<String> getHourIdsByDayId(int dayId) throws SQLException {
        List<String> hourIds = new ArrayList<>();
        String sql = "SELECT hour.id FROM  day JOIN hour ON day.id = day_id WHERE day.id = ? ";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,dayId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    hourIds.add(resultSet.getString("id"));
                }
                return hourIds;
            }
        }
    }

    @Override
    public int getTaskIdByDayId(int dayId, String hourId) throws SQLException {
        String sql = "";
        return 0;

    }


}
