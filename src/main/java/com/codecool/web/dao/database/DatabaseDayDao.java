package com.codecool.web.dao.database;

import com.codecool.web.dao.DayDao;
import com.codecool.web.model.Day;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DatabaseDayDao extends AbstractDaoFactory implements DayDao {
    DatabaseDayDao(Connection connection) {
        super(connection);
    }

    @Override
    public Day add(int scheduleId, String title) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO day (schedule_id, title) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, scheduleId);
            statement.setString(2, title);
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            return new Day(id, scheduleId, title);
        }finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM day WHERE id = ?";
        delete(id, sql);
    }

    @Override
    public void deleteByScheduleId(int scheduleId) throws SQLException {
        String sql = "DELETE FROM day WHERE schedule_id = ?";
        delete(scheduleId, sql);
    }

    @Override
    public void update(int id, String newTitle) throws SQLException {
        String sql = "UPDATE day SET title = ? WHERE day.id = ?";
        update(id, newTitle, sql);
    }

    @Override
    public Day findById(int id) throws SQLException {
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
    public List<Day> findByScheduleId(int scheduleId) throws SQLException {
        List<Day> days = new ArrayList<>();
        String sql = "SELECT * FROM day WHERE schedule_id = ? ORDER BY id ASC";
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

    @Override
    public Day findDayByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM day WHERE title = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,title);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchDay(resultSet);
                }
            }return null;
        }
    }


    private Day fetchDay(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int scheduleId = resultSet.getInt("schedule_id");
        String title = resultSet.getString("title");
        return new Day(id, scheduleId, title);
    }

    @Override
    public void addDueDate(int dayId, Date dueDate) throws SQLException {
        String sql = "INSERT INTO alert(day_id, due_date) VALUES(?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dayId);
            statement.setDate(2, dueDate);
            statement.executeQuery();
        }
    }

    @Override
    public void updateDueDate(int dayId, Date dueDate) throws SQLException {
        String sql = "UPDATE alert SET due_date = ? WHERE day_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1,dueDate);
            statement.setInt(2,dayId);
            statement.executeQuery();
        }
    }

    @Override
    public void deleteDueDateByDayId(int dayId) throws SQLException {
        String sql = "DELETE FROM alert WHERE day_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,dayId);
            statement.executeUpdate();
        }
    }

    @Override
    public Boolean isExists(int dayId) throws SQLException {
        String sql = "SELECT due_date FROM alert WHERE day_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,dayId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public Date getDateByDayId(int dayId) throws SQLException {
        String sql = "SELECT due_date FROM alert WHERE day_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,dayId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getDate("due_date");
                }
                return null;
            }
        }
    }
}
