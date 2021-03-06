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
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@WebServlet("/googleLogin")
public class GoogleLoginServlet extends AbstractServlet {

    final String CLIENT_ID = "144891204676-idis83roj0hnbo5vhpbo9mimppi5c0q0.apps.googleusercontent.com";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try(Connection connection = getConnection(req.getServletContext())) {
            HttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(CLIENT_ID)).build();



            String idTokenString = req.getParameter("idToken");
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");

                UserService userService = new SimpleUserService(connection);
                TaskService taskService = new SimpleTaskService(connection);
                ScheduleService scheduleService = new SimpleScheduleService(connection);

                User user = userService.connectWithGoogle(name, email);
                req.getSession().setAttribute("user", user);

                List<Task> allTask = taskService.findAllByUserId(user.getId());
                List<Schedule> schedules = scheduleService.findAllByUserId(user.getId());
                sendMessage(resp, HttpServletResponse.SC_OK, new UserDto(user, allTask, schedules));

            } else {
                System.out.println("Invalid ID token.");
            }

        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (GeneralSecurityException e) {
            sendMessage(resp, HttpServletResponse.SC_CONFLICT, "Failed verification");
        }

    }
}
