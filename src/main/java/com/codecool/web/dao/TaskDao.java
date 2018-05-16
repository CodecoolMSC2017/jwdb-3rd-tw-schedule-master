package com.codecool.web.dao;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    void add(int userId, String title, String description) throws SQLException;

    void delete(int id) throws SQLException;

    void updateTitle(int id, String newTitle) throws SQLException;

    void updateDescription(int id, String newDescription) throws SQLException;

    Task findById(int id) throws SQLException;

    List<Task> findByUserId(int userId) throws SQLException;

    List<Task> findAll() throws SQLException;
}
