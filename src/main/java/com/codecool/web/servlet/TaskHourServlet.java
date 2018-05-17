package com.codecool.web.servlet;

import com.codecool.web.exception.InvalidArgumentException;
import com.codecool.web.service.TaskHourService;
import com.codecool.web.service.simple.SimpleTaskHourService;
import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/taskHour")
public class TaskHourServlet extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            int taskId = Integer.parseInt(req.getParameter("taskId"));
            String[] hourIds = req.getParameter("hourIds").split(",");
            taskHourService.connectTaskToSchedule(scheduleId, taskId, hourIds);
        } catch (SQLException e) {
            try {
                handleSqlError(resp, e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InvalidArgumentException e) {
            try {
                sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            int taskId = Integer.parseInt(req.getParameter("taskId"));
            String[] newHourIds = req.getParameter("hourIds").split(",");
            taskHourService.updateHours(scheduleId, taskId, newHourIds);
        } catch (SQLException e) {
            try {
                handleSqlError(resp, e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InvalidArgumentException e) {
            try {
                sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskHourService taskHourService = new SimpleTaskHourService(connection);
            JsonNode jsonNode = createJsonNodeFromRequest(req);
            int id = Integer.parseInt(jsonNode.get("id").textValue());
            String disconnectType = jsonNode.get("disconnectType").textValue();
            taskHourService.disconnect(disconnectType, id);
        } catch (SQLException e) {
            try {
                handleSqlError(resp, e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
