package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.DayAlreadyExistsException;
import com.codecool.web.model.Day;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.fasterxml.jackson.core.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
@WebServlet("/protected/date")
public class DateServlet extends AbstractServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);

            User user = getUser(req);
            int userId = user.getId();

            JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
            Day day = objectMapper.readValue(jsonParser,Day.class);
            if(scheduleService.isExists(day.getId())){
                doPut(req,resp);
            }
            scheduleService.addDueDate(day, userId, day.getDueDate());

            Schedule schedule = scheduleService.findById(day.getScheduleId());

            UserDto userDto = getDatas(resp, req);
            userDto.setSchedule(schedule);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (DayAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            User user = getUser(req);
            int userId = user.getId();

            JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
            Day day = objectMapper.readValue(jsonParser, Day.class);

            scheduleService.updateDueDate(day, userId, day.getDueDate());

            Schedule schedule = scheduleService.findById(day.getScheduleId());

            UserDto userDto = getDatas(resp, req);
            userDto.setSchedule(schedule);

            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (DayAlreadyExistsException e) {
            sendMessage(resp, 400, e.getMessage());
        }
    }

        @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try (Connection connection = getConnection(req.getServletContext())) {
                ScheduleService scheduleService = new SimpleScheduleService(connection);
                User user = getUser(req);
                int userId = user.getId();

                JsonParser jsonParser = objectMapper.getFactory().createParser(req.getInputStream());
                Day day = objectMapper.readValue(jsonParser, Day.class);

                scheduleService.deleteDueDateByDayId(day,userId);

                Schedule schedule = scheduleService.findById(day.getScheduleId());

                UserDto userDto = getDatas(resp, req);
                userDto.setSchedule(schedule);

                sendMessage(resp, HttpServletResponse.SC_OK, userDto);
            } catch (SQLException e) {
                handleSqlError(resp, e);
            } catch (DayAlreadyExistsException e) {
                sendMessage(resp, 400, e.getMessage());
            }
    }
}
