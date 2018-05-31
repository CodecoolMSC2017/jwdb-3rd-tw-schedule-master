package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.exception.TaskOverlapException;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskHourService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskHourService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/taskHour")
public class TaskHourServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            int taskId = Integer.parseInt(req.getParameter("taskId"));
            int taskLength = Integer.parseInt(req.getParameter("number"));
            String hourId = req.getParameter("hourId");
            int dayId = Integer.parseInt(req.getParameter("dayId"));
            User user = getUser(req);
            int userId = user.getId();
            taskHourService.handleTaskConnection(userId,dayId,taskLength,scheduleId,taskId,hourId);
            List<Task> taskList = taskService.findAllByUserAndScheduleId(userId,scheduleId);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            Schedule schedule = scheduleService.findById(scheduleId);
            UserDto userDto = new UserDto(user, taskList, schedules);
            userDto.setSchedule(schedule);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (InvalidArgumentException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (TaskOverlapException e) {
            sendMessage(resp,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            int taskId = Integer.parseInt(req.getParameter("taskId"));
            String[] newHourIds = req.getParameter("hourIds").split(",");

            taskHourService.updateHours(scheduleId, taskId, newHourIds);

        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (InvalidArgumentException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            JsonNode jsonNode = createJsonNodeFromRequest(req);
            int id = Integer.parseInt(jsonNode.get("id").textValue());
            taskHourService.disconnect(id);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        }
    }
}
