package com.codecool.web.exception;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException() {
        super("Given argument value is NULL or NOT VALID");
    }
}
