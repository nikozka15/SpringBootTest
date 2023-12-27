package com.nikozka.app.exceptions;

public class DateNotParsedException extends RuntimeException { // todo is it run time ex
    public DateNotParsedException(String errorMessage) { super(errorMessage);
    }
}
