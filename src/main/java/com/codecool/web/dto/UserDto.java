package com.codecool.web.dto;

import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;

import java.util.List;

public class UserDto {
    private User user;
    private List<Task> tasks;
    private List<Schedule> schedules;
    private Schedule schedule;

    public UserDto(User user, List<Task> tasks, List<Schedule> schedules) {
        this.user = user;
        this.tasks = tasks;
        this.schedules = schedules;
    }

    public User getUser() {
        return user;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

}
