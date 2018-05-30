package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/guest")
public class GuestServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(Connection connection = getConnection(req.getServletContext())) {
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            Schedule schedule = scheduleService.findById(Integer.parseInt(req.getParameter("scheduleId")));
            UserDto userDto = new UserDto();
            userDto.setSchedule(schedule);
            sendMessage(resp,HttpServletResponse.SC_OK,userDto);
        } catch (SQLException e) {
            handleSqlError(resp,e);
        }
    }
}
