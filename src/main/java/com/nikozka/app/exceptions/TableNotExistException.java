package com.nikozka.app.exceptions;

public class TableNotExistException extends RuntimeException {
    public TableNotExistException(String errorMessage) { super(errorMessage);
    }
}
