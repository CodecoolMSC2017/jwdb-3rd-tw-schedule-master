package com.codecool.web.service;

import com.codecool.web.exception.DayAlreadyExistsException;
import com.codecool.web.exception.ScheduleAlreadyExistsException;
import com.codecool.web.exception.WrongNumOfDaysException;
import com.codecool.web.model.Day;
import com.codecool.web.model.Hour;
import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleService {

    void createSchedule(String title, String description, int userId, int numOfDays) throws SQLException, WrongNumOfDaysException, ScheduleAlreadyExistsException, DayAlreadyExistsException;

    void deleteSchedule(Schedule schedule)throws SQLException;

    void updateSchedule(int scheduleId, String title, String description)throws SQLException, ScheduleAlreadyExistsException;

    Schedule findById(int scheduleId)throws SQLException;

    List<Schedule> findAll()throws SQLException;

    List<Schedule> findAllByUserId(int userId)throws SQLException;

    Day addDay(int scheduleId, String title, int userId)throws SQLException, DayAlreadyExistsException;

    void updateDay(int dayId, String title, int userId)throws SQLException, DayAlreadyExistsException;

    Day findDayById(int id)throws SQLException;

    List<Day> findDayByScheduleId(int scheduleId)throws SQLException;

    Hour findHourById(int id)throws SQLException;

    List<Hour> findHoursByDayId(int dayId)throws SQLException;


}
