package com.codecool.web.service;

import com.codecool.web.exception.AlreadyRegisteredException;
import com.codecool.web.exception.UserNotFoundException;
import com.codecool.web.exception.WrongPasswordException;
import com.codecool.web.model.User;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    void register(String userName, String password, String email) throws SQLException, AlreadyRegisteredException, NoSuchAlgorithmException;

    User login(String email, String password) throws SQLException, WrongPasswordException, UserNotFoundException, NoSuchAlgorithmException;

    List<User> findAll() throws SQLException;


}
