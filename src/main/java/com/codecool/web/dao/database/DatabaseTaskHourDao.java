package com.codecool.web.dao.database;

import com.codecool.web.dao.TaskHourDao;
import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DatabaseTaskHourDao extends AbstractDaoFactory implements TaskHourDao {
    DatabaseTaskHourDao(Connection connection) {
        super(connection);
    }

    @Override
    public void add(int taskId, int scheduleId, String hourId) throws SQLException {
        String sql = "INSERT INTO task_hour (task_id, schedule_id, hour_ids) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.setInt(2, scheduleId);
            statement.setString(3, hourId);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int taskId, int scheduleId) throws SQLException {
        String sql = "DELETE FROM task_hour WHERE task_id = ? AND schedule_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.setInt(2, scheduleId);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteByTaskId(int taskId) throws SQLException {
        String sql = "DELETE FROM task_hour WHERE task_id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteByScheduleId(int scheduleId) throws SQLException {
        String sql = "DELETE FROM task_hour WHERE schedule_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, scheduleId);
            statement.executeUpdate();
        }
    }


    @Override
    public void update(int taskId, int scheduleId, String... hourIds) throws SQLException, InvalidArgumentException {
        String sql = "UPDATE task_hour SET hour_ids = ? WHERE task_id = ? AND schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, join(hourIds));
            statement.setInt(2, taskId);
            statement.setInt(3, scheduleId);
            statement.executeUpdate();
        }

    }

    @Override
    public List<Task> findTaskByScheduleId(int scheduleId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id,app_user_id,title,description,color FROM task WHERE id IN(SELECT task_id FROM task_hour WHERE schedule_id = ?) ORDER BY id ASC;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, scheduleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(fetchTask(resultSet));
                }
            }
            return tasks;
        }
    }

    public List<String> findHoursByTaskAndScheduleId(int taskId, int scheduleId) throws SQLException {
        List<String> hourIds = new ArrayList<>();
        String hours = "";
        String sql = "SELECT hour_ids FROM task_hour RIGHT JOIN task ON task.id = task_id WHERE task.id = ? AND schedule_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,taskId);
            statement.setInt(2,scheduleId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    hours = resultSet.getString("hour_ids");

                }
            }
        }
        for (int i = 0; i < hours.split(",").length; i++) {
            hourIds.add(hours.split(",")[i]);
        }
        return hourIds;

    }

    @Override
    public Boolean validateHourIds(int userId, int dayId, String hourId) throws SQLException {
        hourId = "%"+hourId+"%";
        String sql = "SELECT * FROM task_hour " +
                "LEFT JOIN schedule ON schedule.id = schedule_id " +
                "LEFT JOIN day ON day.schedule_id = schedule.id " +
                "WHERE app_user_id = ? " +
                "AND day.id = ? " +
                "AND hour_ids LIKE ? ";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,userId);
            statement.setInt(2,dayId);
            statement.setString(3,hourId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return false;
                }
                return true;
            }
        }
    }

    private String join(String... ids) throws InvalidArgumentException {
        String idString = "";
        if (ids.length == 0) {
            throw new InvalidArgumentException();
        } else {
            for (String id : ids) {
                idString += id + ",";
            }

            return idString.trim();
        }

    }
}
