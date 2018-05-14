package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class TaskServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = getUser(req);
            int userId = user.getId();
            String taskTitle = req.getParameter("title");
            String taskDescription = req.getParameter("description");

            taskService.addTask(userId, taskTitle, taskDescription);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = getUser(req);
            int userId = user.getId();
            String taskTitle = req.getParameter("title");
            String taskDescription = req.getParameter("description");
            int taskId = Integer.parseInt(req.getParameter("taskId"));

            taskService.update(taskId,taskTitle,taskDescription);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);

        } catch (SQLException e) {
            handleSqlError(resp,e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = getUser(req);
            int userId = user.getId();
            int taskId = Integer.parseInt(req.getParameter("taskId"));

            taskService.deleteTask(taskId);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);

        } catch (SQLException e) {
            handleSqlError(resp,e);
        }
    }
}
