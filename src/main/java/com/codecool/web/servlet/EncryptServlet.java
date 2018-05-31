package com.codecool.web.servlet;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncryptServlet extends AbstractServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("password");
        String scheduleId = req.getParameter("scheduleId");
        String encrypted = encryptor.encrypt(scheduleId);
        sendMessage(resp,HttpServletResponse.SC_OK,encrypted);
    }
}
