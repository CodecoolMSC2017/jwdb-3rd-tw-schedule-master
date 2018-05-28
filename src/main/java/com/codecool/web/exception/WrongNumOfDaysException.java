package com.codecool.web.exception;

public class WrongNumOfDaysException extends Exception{


    public WrongNumOfDaysException() {
        super("Number of days can not be more than 7 or less than 1");
    }
}
