package com.codecool.web.service;

import com.codecool.web.model.Day;
import com.codecool.web.model.Hour;
import com.codecool.web.model.Schedule;

import java.util.List;

public interface ScheduleService {

    void createSchedule(String title, String description, int userId);

    void deleteSchedule(int scheduleId);

    void updateSchedule(int scheduleId, String title, String description);

    Schedule findById(int scheduleId);

    List<Schedule> findAll();

    List<Schedule> findAllByUserId(int userId);

    void addDay(int scheduleId, String title);

    void updateDay(int dayId, String title);

    Day findDayById(int id);

    List<Day> findDayByScheduleId(int scheduleId);

    Hour findHourById(int id);

    List<Hour> findHoursByDayId(int dayId);

    

}
