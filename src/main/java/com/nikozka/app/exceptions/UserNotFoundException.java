package com.nikozka.app.exceptions;

public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
