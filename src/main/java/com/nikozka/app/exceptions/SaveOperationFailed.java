package com.nikozka.app.exceptions;

public class SaveOperationFailed extends RuntimeException {
    public SaveOperationFailed(String errorMessage) { super(errorMessage);
    }
}
