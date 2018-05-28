package com.codecool.web.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("User not found");
    }
}
