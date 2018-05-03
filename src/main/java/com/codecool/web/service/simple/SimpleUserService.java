package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.exception.AlreadyRegisteredException;
import com.codecool.web.exception.UserNotFoundException;
import com.codecool.web.exception.WrongPasswordException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class SimpleUserService implements UserService {

    UserDao userDao;

    public SimpleUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void register(String userName, String password, String email) throws SQLException, AlreadyRegisteredException {
        if (userDao.find(email) == null) {
            userDao.insertUser(email, userName, password, "user");
        } else {
            throw new AlreadyRegisteredException();
        }
    }

    @Override
    public User login(String email, String password) throws SQLException, WrongPasswordException, UserNotFoundException {
        User userByEmail = userDao.find(email);
        if (userByEmail != null) {
            User userByEmailAndPassword = userDao.find(email, password);
            if (userByEmailAndPassword != null) {
                return userByEmailAndPassword;
            } else {
                throw new WrongPasswordException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }
}
