package com.codecool.web.servlet;

import com.codecool.web.dto.MessageDto;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/protected/encrypt")
public class EncryptServlet extends AbstractServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("password");
        String scheduleId = req.getParameter("scheduleId");
        String encrypted = encryptor.encrypt(scheduleId);
        MessageDto messageDto = new MessageDto(encrypted);
        sendMessage(resp,HttpServletResponse.SC_OK,messageDto);
    }
}
