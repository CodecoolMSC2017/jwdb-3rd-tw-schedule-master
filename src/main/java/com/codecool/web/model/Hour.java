package com.codecool.web.model;

public class Hour extends AbstractModel {

    private int dayId;
    private int value;
    private Task task;

    public Hour(int id, int dayId, int value) {
        super(id);
        this.dayId = dayId;
        this.value = value;
    }

    public int getDayId() {
        return dayId;
    }

    public int getValue() {
        return value;
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
