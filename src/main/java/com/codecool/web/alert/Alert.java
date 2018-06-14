package com.codecool.web.alert;

import com.codecool.web.dao.AlertDao;
import com.codecool.web.dao.HourDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Alert implements org.quartz.Job {
    Connection connection;
    public Alert(Connection connection){
    this.connection = connection;
    }

    AlertDao alertDao = (AlertDao) AbstractDaoFactory.getDao("alert",connection);
    HourDao hourDao = (HourDao) AbstractDaoFactory.getDao("hour",connection);
    TaskDao taskDao = (TaskDao) AbstractDaoFactory.getDao("task",connection);
    UserDao userDao = (UserDao) AbstractDaoFactory.getDao("user",connection);
    static HashMap<Integer,List<String>> hoursByDayId = new HashMap<>();


    void prepeareAlerts() throws SQLException {
        for(int index:alertDao.getTodaysAlerts()){
            hoursByDayId.put(index,alertDao.getHourIdsByDayId(index));
        }
    }

    void handleAlert() throws SQLException {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        for (Map.Entry<Integer,List<String>> entry : hoursByDayId.entrySet()){
           for (String hourId : entry.getValue()){
               int taskId = alertDao.getTaskIdByDayId(entry.getKey(),hourId);
               if(taskId != -1){
                   String taskName = taskDao.findById(taskId).getTitle();
                   String userName = userDao.findByDayId(entry.getKey()).getUserName();
                   String email = userDao.findByDayId(entry.getKey()).getEmail();
                   int time = hourDao.findById(Integer.parseInt(hourId)).getValue();
                   if( time-currentHour >= 1 && time-currentHour < 2 ){
                        sendEmail(taskName,userName,time,email);
                   }
               }
           }
        }
    }

    void sendEmail(String taskName,String userName,int dueDate,String email){
        String to = email;

        // Sender's email ID needs to be mentioned
        String from = "reminder.myschedule@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("smtp.gmail.com", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("REMINDER from myschedule.site");

            // Now set the actual message
            message.setText(String.format("Dear %s,\nyour upcoming %s task starts at %d:00",userName,taskName,dueDate));
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            handleAlert();
        } catch (SQLException e) {
            System.out.println("The sqlquery return a null or nothing happening");
        }
    }
}
