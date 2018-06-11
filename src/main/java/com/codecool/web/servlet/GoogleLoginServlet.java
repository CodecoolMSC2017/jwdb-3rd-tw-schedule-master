package com.codecool.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;


@WebServlet("/googleLogin")
public class GoogleLoginServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(Connection connection = getConnection(req.getServletContext())) {
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            UserService userService = new SimpleUserService(connection);

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
