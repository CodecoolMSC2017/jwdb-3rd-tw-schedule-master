package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.DayAlreadyExistsException;
import com.codecool.web.exception.ScheduleAlreadyExistsException;
import com.codecool.web.exception.WrongNumOfDaysException;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
            UserDto userDto = new UserDto();

            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks ;

            userDto.setSchedules(schedules);
            userDto.setUser(user);

            String scheduleIdFromReq = req.getParameter("scheduleId");

            if (scheduleIdFromReq != null) {
                int scheduleId = Integer.parseInt(scheduleIdFromReq);
                Schedule schedule = scheduleService.findById(scheduleId);
                scheduleIdFromReq = Integer.toString(schedule.getId());

                userDto.setSchedule(schedule);
                tasks = taskService.findAllByUserAndScheduleId(userId, Integer.parseInt(scheduleIdFromReq));

            } else {
                tasks = taskService.findAllByUserId(userId);
            }

            userDto.setTasks(tasks);
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
        } catch (WrongNumOfDaysException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ScheduleAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (DayAlreadyExistsException e) {
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

            JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
            Schedule schedule = objectMapper.readValue(jsonParser, Schedule.class);

            scheduleService.updateSchedule(schedule);


            doGet(req, resp);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ScheduleAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);

            JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
            Schedule schedule = objectMapper.readValue(jsonParser, Schedule.class);

            scheduleService.deleteSchedule(schedule);

            doGet(req, resp);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        }
    }
}
