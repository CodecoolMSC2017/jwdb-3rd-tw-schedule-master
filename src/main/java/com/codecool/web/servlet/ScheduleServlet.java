package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.TooManyDaysException;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.databind.JsonNode;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);

            String scheduleIdFromReq = req.getParameter("scheduleId");

            if (scheduleIdFromReq != null ) {
                int scheduleId = Integer.parseInt(scheduleIdFromReq);
                Schedule schedule = scheduleService.findById(scheduleId);
                userDto.setSchedule(schedule);
            }

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            User user = getUser(req);
            int userId = user.getId();

            String scheduleTitle = req.getParameter("title");
            String scheduleDescription = req.getParameter("description");
            int scheduleDays = Integer.parseInt(req.getParameter("days"));

            scheduleService.createSchedule(scheduleTitle, scheduleDescription, userId, scheduleDays);

            doGet(req, resp);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (TooManyDaysException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);


            JsonNode jsonNode = createJsonNodeFromRequest(req);

            int scheduleId = Integer.parseInt(jsonNode.get("scheduleId").textValue());
            String scheduleTitle = jsonNode.get("title").textValue();
            String scheduleDescription = jsonNode.get("description").textValue();

            scheduleService.updateSchedule(scheduleId, scheduleTitle, scheduleDescription);
            schedules = scheduleService.findAllByUserId(userId);
            Schedule schedule = scheduleService.findById(scheduleId);
            UserDto userDto = new UserDto(user, tasks, schedules);
            userDto.setSchedule(schedule);
            sendMessage(resp, 200, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (Exception e) {
            sendMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);

            JsonNode jsonNode = createJsonNodeFromRequest(req);

            int scheduleId = Integer.parseInt(jsonNode.get("scheduleId").textValue());

            scheduleService.deleteSchedule(scheduleId);

            doGet(req, resp);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        }
    }
}
