package com.codecool.web.exception;

public class AlreadyRegisteredException extends Exception {
    public AlreadyRegisteredException() {
        super("User already registered");
    }
}
