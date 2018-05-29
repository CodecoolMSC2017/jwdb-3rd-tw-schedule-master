package com.codecool.web.model;

import java.util.List;

public class Schedule extends AbstractModel {

    private int userId;
    private String title;
    private String description;
    private List<Day> days;

    public Schedule(int id, int userId, String title, String description) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
