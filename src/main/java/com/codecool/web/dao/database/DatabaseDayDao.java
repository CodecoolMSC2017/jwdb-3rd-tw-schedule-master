package com.codecool.web.dao.database;

import com.codecool.web.dao.DayDao;
import com.codecool.web.model.Day;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseDayDao extends AbstractDaoFactory implements DayDao {
    DatabaseDayDao(Connection connection) {
        super(connection);
    }

    @Override
    public void addDay(int scheduleId, String title) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO day (schedule_id, title) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            statement.setString(2, title);
            executeInsert(statement);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void deleteDay(int id) throws SQLException {
        String sql = "DELETE FROM day WHERE id = ?";
        delete(id, sql);
    }

    @Override
    public void updateDay(int id, String newTitle) throws SQLException {
        String sql = "DELETE FROM task WHERE task.id = ?";
        update(id, newTitle, sql);
    }

    @Override
    public Day findDayById(int id) throws SQLException {
        String sql = "SELECT * FROM task WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetchDay(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<Day> findDayByScheduleId(int scheduleId) throws SQLException {
        List<Day> days = new ArrayList<>();
        String sql = "SELECT * FROM day WHERE schedule_id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    days.add(fetchDay(resultSet));
                }
            }
        }
        return days;
    }


    private Day fetchDay(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int scheduleId = resultSet.getInt("schedule_id");
        String title = resultSet.getString("title");
        return new Day(id, scheduleId, title);
    }
}
