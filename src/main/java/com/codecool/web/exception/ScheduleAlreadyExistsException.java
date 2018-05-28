package com.codecool.web.exception;

import com.codecool.web.model.Schedule;

public class ScheduleAlreadyExistsException extends Exception {
    public ScheduleAlreadyExistsException() {
        super("Schedule with this name already exists!");
    }
}
