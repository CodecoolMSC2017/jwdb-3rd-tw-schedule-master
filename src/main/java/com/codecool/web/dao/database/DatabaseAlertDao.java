package com.codecool.web.dao.database;

import com.codecool.web.dao.AlertDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DatabaseAlertDao extends AbstractDaoFactory implements AlertDao{
    public DatabaseAlertDao(Connection connection){super(connection);}

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

        String sql = "SELECT task.id " +
                "FROM task_hour \n" +
                "JOIN task ON task_hour.task_id = task.id \n" +
                "JOIN schedule ON schedule.id = task_hour.schedule_id \n" +
                "JOIN day ON day.schedule_id = schedule.id\n" +
                "JOIN app_user ON schedule.app_user_id = app_user.id \n" +
                "WHERE ? = ANY(regexp_split_to_array(hour_ids,E',')) AND day.id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,hourId);
            statement.setInt(2,dayId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getInt("id");
                }
                return -1;
            }
        }

    }


}
