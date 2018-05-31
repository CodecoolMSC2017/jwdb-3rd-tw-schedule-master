package com.codecool.web.exception;

public class TaskOverlapException extends Exception{
    public TaskOverlapException() {
        super("Task overlaps with another task");
    }
}
