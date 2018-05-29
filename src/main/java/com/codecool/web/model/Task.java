package com.codecool.web.model;

import java.util.Objects;

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

    public Task() {
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
