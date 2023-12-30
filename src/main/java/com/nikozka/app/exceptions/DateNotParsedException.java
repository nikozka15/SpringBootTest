package com.nikozka.app.exceptions;

public class DateNotParsedException extends RuntimeException {
    public DateNotParsedException(String errorMessage) { super(errorMessage);
    }
}
