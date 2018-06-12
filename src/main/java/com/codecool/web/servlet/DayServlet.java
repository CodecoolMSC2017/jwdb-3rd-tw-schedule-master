package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.DayAlreadyExistsException;
import com.codecool.web.model.Day;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.fasterxml.jackson.core.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/protected/day")
public class DayServlet extends AbstractServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException { // TODO fix rename day and schedule
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            User user = getUser(req);
            int userId = user.getId();

            JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
            Day day = objectMapper.readValue(jsonParser, Day.class);

            if (day.getDueDate() != null) {
                int dayId = day.getId();
                Date dueDate = day.getDueDate();
                scheduleService.addDueDate(dayId, dueDate);
            }

            scheduleService.updateDay(day, userId);
            Schedule schedule = scheduleService.findById(day.getScheduleId());

            UserDto userDto = getDatas(resp,req);
            userDto.setSchedule(schedule);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (DayAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
