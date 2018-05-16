package com.codecool.web.exception;

public class TooManyDaysException extends Exception{


    public TooManyDaysException() {
        super("Number of days can not be more than 7");
    }
}
