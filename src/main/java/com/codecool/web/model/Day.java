package com.codecool.web.model;

import java.sql.Date;
import java.util.List;

public class Day extends AbstractModel {

    private int scheduleId;
    private String title ;
    private List<Hour> hours;
    private Date dueDate;

    public Day(int id, int scheduleId, String title) {
        super(id);
        this.scheduleId = scheduleId;
        this.title = title;
    }

    public Day() {
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
