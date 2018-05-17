package com.codecool.web.dto;

import com.codecool.web.model.Schedule;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;

import java.util.List;

public class UserDto {
    private User user;
    private List<Task> tasks;
    private List<Schedule> schedules;
    private List<User> users;
    private Schedule schedule;


    public UserDto(User user, List<Task> tasks, List<Schedule> schedules) {
        this.user = user;
        this.tasks = tasks;
        this.schedules = schedules;
    }

    public UserDto(List<User> users, List<Task> tasks, User user) {
        this.user = user;
        this.tasks = tasks;
        this.users = users;
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
