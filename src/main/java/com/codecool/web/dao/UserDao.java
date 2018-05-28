package com.codecool.web.dao;

import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;


public interface UserDao {

    void insert(String email, String userName, String password, String role) throws SQLException;

    User findByEmail(String email) throws SQLException;

    User findByEmail(String email, String password) throws SQLException;

    User findById(int id) throws SQLException;

    List<User> findAll() throws SQLException;

    void deleteUserbyId(int id) throws SQLException;
}
