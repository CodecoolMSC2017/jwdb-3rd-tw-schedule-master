package com.codecool.web.servlet;

import com.codecool.web.dto.MessageDto;
import com.codecool.web.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractServlet extends HttpServlet {

    private final ObjectMapper om = new ObjectMapper();
    final Logger logger = Logger.getLogger(LoginServlet.class);

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
        om.writeValue(resp.getOutputStream(), object);
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
}

