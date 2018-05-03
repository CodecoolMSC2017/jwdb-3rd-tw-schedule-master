package com.codecool.web.model;

public class Task extends AbstractModel {

    private int userId;
    private String title;
    private String description;

    public Task(int id, int userId, String title, String description) {
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
}
