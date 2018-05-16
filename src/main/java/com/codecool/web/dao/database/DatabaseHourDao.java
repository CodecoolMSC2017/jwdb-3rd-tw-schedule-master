package com.codecool.web.dao.database;

import com.codecool.web.dao.HourDao;
import com.codecool.web.model.Hour;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseHourDao extends AbstractDaoFactory implements HourDao {

    DatabaseHourDao(Connection connection) {
        super(connection);
    }

    @Override
    public void add(int dayId, int value) throws SQLException {
        String sql = "INSERT INTO hour (day_id,value)VALUES(?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dayId);
            statement.setInt(2, value);
            executeInsert(statement);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM hour WHERE id = ?;";
        delete(id, sql);

    }

    @Override
    public void deleteByDayId(int dayId) throws SQLException {
        String sql = "DELETE FROM hour WHERE day_id = ?";
        delete(dayId, sql);
    }


    @Override
    public Hour findById(int id) throws SQLException {
        String sql = "SELECT * FROM hour WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return fetch(resultSet);
            }
        }

    }

    @Override
    public List<Hour> findByDayId(int dayId) throws SQLException {
        List<Hour> hours = new ArrayList<>();
        String sql = "SELECT * FROM hour WHERE day_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dayId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hours.add(fetch(resultSet));
                }
            }
        }
        return hours;

    }

    private Hour fetch(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int dayId = resultSet.getInt("day_id");
        int value = resultSet.getInt("value");
        return new Hour(id, dayId, value);
    }


}
