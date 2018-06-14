package com.codecool.web.alert;

import com.codecool.web.dao.AlertDao;
import com.codecool.web.dao.HourDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.AbstractDaoFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Alert implements org.quartz.Job {
    private static HashMap<Integer, List<Integer>> hoursByDayId = new HashMap<>();
    private static List<Integer> alertableTasksIds = new ArrayList<>();
    private static HashMap<Integer, Integer> alerts = new HashMap<>();
    private static List<Integer> alreadyAlerted = new ArrayList<>();
    private Connection connection;
    private AlertDao alertDao;
    private HourDao hourDao;
    private TaskDao taskDao;
    private UserDao userDao;

    public Alert() {
        this.connection = null;
    }

    void prepeareAlerts() throws SQLException {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        for (int index : alertDao.getTodaysAlerts()) {
            List<Integer> sortList = alertDao.getHourIdsByDayId(index);
            hoursByDayId.put(index, sortList);

        }
        for (Map.Entry<Integer, List<Integer>> entry : hoursByDayId.entrySet()) {
            boolean found = false;
            for (int hourId : entry.getValue()) {
                int time = hourDao.findById(hourId).getValue();
                int taskId = alertDao.getTaskIdByDayId(entry.getKey(), Integer.toString(hourId));

                if (time - currentHour > 1 && time - currentHour < 3 && !alertableTasksIds.contains(taskId) && taskId != -1 && !found) {

                    found = true;
                    alertableTasksIds.add(taskId);

                }
            }
        }

    }

    void handleAlert() throws SQLException {
        for (Map.Entry<Integer, Integer> entry : alerts.entrySet()) {
            int hourId = entry.getValue();
            int taskId = alertDao.getTaskIdByDayId(entry.getKey(), Integer.toString(hourId));

            String taskName = taskDao.findById(taskId).getTitle();
            System.out.println(taskName);
            String userName = userDao.findByDayId(entry.getKey()).getUserName();
            String email = userDao.findByDayId(entry.getKey()).getEmail();

            int time = hourDao.findById(hourId).getValue();
            sendEmailGmail(email, taskName, userName, time);
            alreadyAlerted.add(taskId);


        }
    }


    void sendEmailGmail(String email, String taskName, String userName, int dueDate) {

        final String username = "reminder.myschedule@gmail.com";
        final String password = "myscheduleadmin";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("reminder.myschedule@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Task reminder");
            message.setText(String.format("Dear %s,\nyour upcoming %s task starts at %d:00", userName, taskName, dueDate));

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    void reloadAlerts() throws SQLException {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (Map.Entry<Integer, List<Integer>> entry : hoursByDayId.entrySet()) {
            List<Integer> sortList = entry.getValue();
            Collections.sort(sortList);
            boolean found = false;
            for (int hourId : sortList) {
                int taskId = alertDao.getTaskIdByDayId(entry.getKey(), Integer.toString(hourId));
                if (alertableTasksIds.contains(taskId) && !map.containsKey(entry.getKey()) && !found && !alreadyAlerted.contains(taskId)) {
                    found = true;
                    map.put(entry.getKey(), hourId);
                    System.out.println(hourId);
                }
            }
        }
        System.out.println(map.size());
        alerts.clear();
        alerts.putAll(map);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DataSource dataSource = (DataSource) jobExecutionContext.getJobDetail().getJobDataMap().get("dataSource");
        try {
            connection = dataSource.getConnection();

            setConnection(connection);
            alertDao = (AlertDao) AbstractDaoFactory.getDao("alert", connection);
            hourDao = (HourDao) AbstractDaoFactory.getDao("hour", connection);
            taskDao = (TaskDao) AbstractDaoFactory.getDao("task", connection);
            userDao = (UserDao) AbstractDaoFactory.getDao("user", connection);

            prepeareAlerts();
            reloadAlerts();
            handleAlert();

            Date date = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            if(currentHour == 0 || currentHour == 24){
                hoursByDayId = new HashMap<>();
                alertableTasksIds = new ArrayList<>();
                alerts = new HashMap<>();
                alreadyAlerted = new ArrayList<>();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

