package com.nikozka.app.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String errorMessage) { super(errorMessage);
    }
}
