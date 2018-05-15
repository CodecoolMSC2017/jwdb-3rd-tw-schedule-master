package com.codecool.web.dao.database;

import com.codecool.web.dao.HourDao;
import com.codecool.web.model.Hour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DatabaseHourDao extends AbstractDaoFactory implements HourDao {

    DatabaseHourDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addHour(int dayId, int value) throws SQLException {
        String sql = "INSERT INTO hour (dayId,value)VALUES(?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dayId);
            statement.setInt(2, value);
            executeInsert(statement);
        }
    }

    @Override
    public void deleteHour(int id) throws SQLException {
        String sql = "DELETE FROM hour WHERE id = ?;";
        delete(id, sql);

    }

    @Override
    public Hour findHourById(int id) throws SQLException {
        String sql = "SELECT * FROM hour WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return fetchHour(resultSet);
            }
        }

    }

    @Override
    public List<Hour> findHoursByDayId(int dayId) throws SQLException {
        List<Hour> hours = new ArrayList<>();
        String sql = "SELECT * FROM hour;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                hours.add(fetchHour(resultSet));
            }
        }
        return hours;

    }


    private Hour fetchHour(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int dayId = resultSet.getInt("day_id");
        int value = resultSet.getInt("value");
        return new Hour(id, dayId, value);
    }


}
