package com.codecool.web.servlet;

import com.codecool.web.dto.UserDto;
import com.codecool.web.exception.TaskAlreadyExistsException;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleTaskService;
import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/task")
public class TaskServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserDto userDto = getDatas(req);
            sendMessage(resp, HttpServletResponse.SC_OK, userDto);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskService taskService = new SimpleTaskService(connection);
            User user = getUser(req);
            int userId = user.getId();
            String taskTitle = req.getParameter("title");
            String taskDescription = req.getParameter("description");
            String taskColor = req.getParameter("color");

            taskService.addTask(userId, taskTitle, taskDescription, taskColor);

            doGet(req, resp);
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskService taskService = new SimpleTaskService(connection);

            BufferedReader reader = req.getReader();

            Task task = objectMapper.readValue(reader, Task.class);

            taskService.update(task);
            doGet(req, resp);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (TaskAlreadyExistsException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskService taskService = new SimpleTaskService(connection);
            JsonNode jsonNode = createJsonNodeFromRequest(req);

            int taskId = Integer.parseInt(jsonNode.get("taskId").textValue());

            taskService.deleteTask(taskId);

            doGet(req, resp);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        }
    }
}
