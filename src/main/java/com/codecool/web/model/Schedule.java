package com.codecool.web.model;

public class Schedule extends AbstractModel {

    private int userID;
    private String title;
    private String description;

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
}
