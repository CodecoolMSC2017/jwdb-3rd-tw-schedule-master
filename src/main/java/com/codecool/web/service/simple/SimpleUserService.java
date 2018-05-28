package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import com.codecool.web.dto.AdminDto;
import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.AlreadyRegisteredException;
import com.codecool.web.exception.UserNotFoundException;
import com.codecool.web.exception.WrongPasswordException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SimpleUserService implements UserService {

    private final UserDao userDao;

    public SimpleUserService(Connection connection) {
        userDao = (UserDao) AbstractDaoFactory.getDao("user", connection);
    }

    @Override
    public void register(String userName, String password, String email) throws SQLException, AlreadyRegisteredException, NoSuchAlgorithmException {
        if (userDao.find(email) == null) {
            password = encrypt(password);
            userDao.insert(email, userName, password, "user");
        } else {
            throw new AlreadyRegisteredException();
        }
    }

    @Override
    public User login(String email, String password) throws SQLException, WrongPasswordException, UserNotFoundException, NoSuchAlgorithmException {
        User userByEmail = userDao.find(email);
        if (userByEmail != null) {
            password = encrypt(password);
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

    private String encrypt(String password) throws NoSuchAlgorithmException {
        StringBuffer hexString = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest();

        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }
}
