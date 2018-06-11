package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/googleLogin")
public class GoogleLoginServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(Connection connection = getConnection(req.getServletContext())) {
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            UserService userService = new SimpleUserService(connection);
            TaskService taskService = new SimpleTaskService(connection);
            ScheduleService scheduleService = new SimpleScheduleService(connection);

            User user = userService.connectWithGoogle(name, email);
            req.getSession().setAttribute("user", user);


            List<Task> allTask = taskService.findAllByUserId(user.getId());
            List<Schedule> schedules = scheduleService.findAllByUserId(user.getId());
            sendMessage(resp, HttpServletResponse.SC_OK, new UserDto(user, allTask, schedules));

        } catch (SQLException e) {
            handleSqlError(resp, e);
        }

    }
}
