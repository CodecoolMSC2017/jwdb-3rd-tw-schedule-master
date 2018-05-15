package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/schedule")
public class ScheduleServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);
            sendMessage(resp, HttpServletResponse.SC_OK, userDto);

        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            String scheduleTitle = req.getParameter("title");
            String scheduleDescription = req.getParameter("description");

            scheduleService.createSchedule(scheduleTitle, scheduleDescription, userId);
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
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            int scheduleId = Integer.parseInt(req.getParameter("create-schedule"));
            String scheduleTitle = req.getParameter("title");
            String scheduleDescription = req.getParameter("description");

            scheduleService.updateSchedule(scheduleId, scheduleTitle, scheduleDescription);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);

        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (Exception e) {
            sendMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
