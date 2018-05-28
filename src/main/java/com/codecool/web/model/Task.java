package com.codecool.web.model;

public class Task extends AbstractModel {

    private int userId;
    private String title;
    private String description;
    private String color;

    public Task(int id, int userId, String title, String description, String color) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.color = color;
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

    public String getColor() {
        return  color;
    }
}
