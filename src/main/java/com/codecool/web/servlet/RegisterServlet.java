package com.codecool.web.servlet;

import com.codecool.web.exception.AlreadyRegisteredException;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/register")
public final class RegisterServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            UserService userService = new SimpleUserService(connection);

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            userService.register(name, password, email);

            sendMessage(resp, HttpServletResponse.SC_OK, "Registration is successful, you can login now!");
        } catch (SQLException ex) {
            handleSqlError(resp, ex);
        } catch (AlreadyRegisteredException e) {
            sendMessage(resp, HttpServletResponse.SC_CONFLICT, "This email is already in use!");
        } catch (NoSuchAlgorithmException e) {
            sendMessage(resp, HttpServletResponse.SC_EXPECTATION_FAILED, "Unexpected error occurred");
        }
    }
}
