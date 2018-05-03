package com.codecool.web.model;

public class Day extends AbstractModel {

    private int scheduleId;
    private String title ;

    public Day(int id, int scheduleId, String title) {
        super(id);
        this.scheduleId = scheduleId;
        this.title = title;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getTitle() {
        return title;
    }
}
