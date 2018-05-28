package com.codecool.web.exception;

public class TaskAlreadyExistsException extends Exception{
    public TaskAlreadyExistsException() {
        super("Task already exists!");
    }
}
