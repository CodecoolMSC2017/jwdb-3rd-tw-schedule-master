package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.DayAlreadyExistsException;
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

@WebServlet("/protected/day")
public class DayServlet extends AbstractServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException { // TODO fix rename day and schedule
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();

            JsonNode jsonNode = createJsonNodeFromRequest(req);

            int dayId = Integer.parseInt(jsonNode.get("dayId").textValue());
            String dayTitle = jsonNode.get("title").textValue();
            int scheduleId = Integer.parseInt(jsonNode.get("scheduleId").textValue());

            scheduleService.updateDay(dayId, dayTitle, userId);
            Schedule schedule = scheduleService.findById(scheduleId);

            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            List<Task> tasks = taskService.findAllByUserId(userId);
            UserDto userDto = new UserDto(user, tasks, schedules);
            userDto.setSchedule(schedule);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (DayAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
