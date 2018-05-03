package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseUserDao  extends AbstractDao implements UserDao {


    public DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public void insertUser(String email, String user_name, String password, String role) throws SQLException {

    }

    @Override
    public User find(String email) throws SQLException {
        return null;
    }

    @Override
    public User find(String email, String password) throws SQLException {
        return null;
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        return null;
    }
}
