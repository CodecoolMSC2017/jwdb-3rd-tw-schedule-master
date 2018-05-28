package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DatabaseUserDao extends AbstractDaoFactory implements UserDao {


    DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(String email, String userName, String password, String role) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String sql = "INSERT INTO app_user(email, user_name, password, role) VALUES (?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email);
            statement.setString(2, userName);
            statement.setString(3, password);
            statement.setString(4, role);
            executeInsert(statement);
        }finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetch(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetch(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email, String password) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return fetch(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM app_user ORDER BY id ASC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(fetch(resultSet));
            }
        }
        return users;
    }

    @Override
    public void deleteUserbyId(int id) throws SQLException {
        String sql = "DELETE FROM app_user\n" +
                " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private User fetch(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String userName = resultSet.getString("user_name");
        String password = resultSet.getString("password");
        String role = resultSet.getString("role");
        return new User(id, email, userName, password, role);
    }
}
