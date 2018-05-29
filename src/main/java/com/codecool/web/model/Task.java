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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return userId == task.userId &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(color, task.color);
    }

}
