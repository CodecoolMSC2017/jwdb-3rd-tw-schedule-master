package com.codecool.web.model;

import java.util.List;

public class Schedule extends AbstractModel {

    private int userID;
    private String title;
    private String description;
    private List<Day> days;

    public Schedule(int id, int userID, String title, String description) {
        super(id);
        this.userID = userID;
        this.title = title;
        this.description = description;
    }

    public int getUserID() {
        return userID;
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
}
