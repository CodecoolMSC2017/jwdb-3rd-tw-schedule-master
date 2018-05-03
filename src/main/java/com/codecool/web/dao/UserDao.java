package com.codecool.web.dao;

import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;


public interface UserDao {

    void insertUser(String email, String user_name, String password, String role) throws SQLException;

    User find(String email) throws SQLException;

    User find(String email, String password) throws SQLException;

    List<User> findAll() throws SQLException;
}
