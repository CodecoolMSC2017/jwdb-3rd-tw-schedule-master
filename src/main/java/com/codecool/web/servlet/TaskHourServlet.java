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
            if (req.getParameter("scheduleId").equals("")) {
                sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, new TaskOverlapException().getMessage());
            } else {
                int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
                int taskId = Integer.parseInt(req.getParameter("taskId"));
                int taskLength = Integer.parseInt(req.getParameter("number"));
                String hourId = req.getParameter("hourId");
                int dayId = Integer.parseInt(req.getParameter("dayId"));
                User user = getUser(req);
                int userId = user.getId();
                taskHourService.handleTaskConnection(userId, dayId, taskLength, scheduleId, taskId, hourId);

                Schedule schedule = scheduleService.findById(scheduleId);
                UserDto userDto = getDatas(resp,req);
                userDto.setSchedule(schedule);

                sendMessage(resp, HttpServletResponse.SC_OK, userDto);
            }
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (InvalidArgumentException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (TaskOverlapException e) {
            sendMessage(resp,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            JsonNode jsonNode = createJsonNodeFromRequest(req);
            int taskId = Integer.parseInt(jsonNode.get("taskId").textValue());
            int scheduleId = Integer.parseInt(jsonNode.get("scheduleId").textValue());
            taskHourService.disconnect(taskId,scheduleId);
            User user = getUser(req);
            int userId = user.getId();
            List<Task> taskList = taskService.findAllByUserAndScheduleId(userId, scheduleId);
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, taskList, schedules);
            Schedule schedule = scheduleService.findById(scheduleId);
            userDto.setSchedule(schedule);
            sendMessage(resp,HttpServletResponse.SC_OK,userDto);


        } catch (SQLException e) {
            handleSqlError(resp, e);
        }
    }
}
