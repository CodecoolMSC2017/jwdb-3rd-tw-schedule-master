package com.codecool.web.servlet;

import com.codecool.web.dto.MessageDto;
import com.codecool.web.dto.UserDto;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleScheduleService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

abstract class AbstractServlet extends HttpServlet {

    final ObjectMapper objectMapper = new ObjectMapper();
    final Logger logger = LogManager.getLogger(LoginServlet.class);

    Connection getConnection(ServletContext sce) throws SQLException {
        DataSource dataSource = (DataSource) sce.getAttribute("dataSource");
        return dataSource.getConnection();
    }

    User getUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }


    void sendMessage(HttpServletResponse resp, int status, String message) throws IOException {
        sendMessage(resp, status, new MessageDto(message));
    }

    void sendMessage(HttpServletResponse resp, int status, Object object) throws IOException {
        resp.setStatus(status);
        objectMapper.writeValue(resp.getOutputStream(), object);
    }

    void handleSqlError(HttpServletResponse resp, SQLException ex) throws IOException {
        sendMessage(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        ex.printStackTrace();
    }

    String jsonToString(BufferedReader reader) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }
        return jb.toString();
    }

    JsonNode createJsonNodeFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        String json = jsonToString(reader);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    UserDto getDatas(HttpServletRequest req) throws SQLException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskService taskService = new SimpleTaskService(connection);
            ScheduleService scheduleService = new SimpleScheduleService(connection);
            User user = getUser(req);
            int userId = user.getId();
            List<Task> tasks ;
            String currentId = req.getParameter("currentScheduleId");
            if (currentId == null || currentId.equals("null")) {
                 tasks = taskService.findAllByUserId(userId);
            }
            else{
                tasks = taskService.findAllByUserAndScheduleId(userId,Integer.parseInt(currentId));
            }
            List<Schedule> schedules = scheduleService.findAllByUserId(userId);
            return new UserDto(user, tasks, schedules);
        }
    }
}

