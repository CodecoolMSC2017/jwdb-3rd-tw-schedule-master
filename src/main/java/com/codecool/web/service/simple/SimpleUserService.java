package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
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
        if (userDao.findByEmail(email) == null) {
            password = encrypt(password);
            userDao.insert(email, userName, password, "user");
        } else {
            throw new AlreadyRegisteredException();
        }
    }

    @Override
    public User login(String email, String password) throws SQLException, WrongPasswordException, UserNotFoundException, NoSuchAlgorithmException {
        User userByEmail = userDao.findByEmail(email);
        if (userByEmail != null) {
            password = encrypt(password);
            User userByEmailAndPassword = userDao.findByEmail(email, password);
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
    public User getByEmail(String email) throws SQLException {
        return userDao.getByEmail(email);
    }

    @Override
    public User connectWithGoogle(String userName, String email) throws SQLException {
        if (userDao.findByEmail(email) == null) {
            userDao.insert(email, userName, null, "user");
        }
        return userDao.findByEmail(email);
    }

    @Override
    public User findById(int id) throws SQLException {
        return userDao.findById(id);
    }


    @Override
    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }

    @Override
    public void deleteUser(int userId) throws SQLException {
        userDao.deleteUserbyId(userId);
    }

    private String encrypt(String password) throws NoSuchAlgorithmException {
        byte[] plainText = password.getBytes();

        MessageDigest md = MessageDigest.getInstance("MD5");

        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }
}
