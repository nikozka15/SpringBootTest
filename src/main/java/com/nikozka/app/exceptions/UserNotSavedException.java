package com.nikozka.app.exceptions;

public class UserNotSavedException extends RuntimeException { // todo is it run time ex
    public UserNotSavedException(String errorMessage) { super(errorMessage);
    }
}
