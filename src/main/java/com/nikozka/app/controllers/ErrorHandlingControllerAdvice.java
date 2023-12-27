package com.nikozka.app.controllers;

import com.nikozka.app.exceptions.DateNotParsedException;
import com.nikozka.app.exceptions.UserNotFoundException;
import com.nikozka.app.exceptions.UserNotSavedException;
import com.nikozka.app.model.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.stream.Collectors;

@ControllerAdvice
class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLSyntaxErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleSQLSyntaxErrorException(SQLSyntaxErrorException ex)
            throws SQLSyntaxErrorException {
        if (ex.getMessage().equals("Table 'db_test.product_entity' doesn't exist")) { // todo

            return new ResponseEntity<>(new ErrorResponse(
                    "Before requesting all products execute POST /products/add "),
                    HttpStatus.NOT_FOUND);
        }
        throw new SQLSyntaxErrorException(ex);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException ex) throws SQLIntegrityConstraintViolationException {
        if (ex.getMessage().startsWith("Duplicate entry") && ex.getMessage().contains("for key 'user_entity.username'")) {

            return new ResponseEntity<>(new ErrorResponse(
                    "A user with the specified username already exists"),
                    HttpStatus.CONFLICT);
        }
        throw new SQLIntegrityConstraintViolationException(ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UserNotFoundException ex) {
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(new ErrorResponse(errorMessages), status);
    }
    @ExceptionHandler(UserNotSavedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUserNotSavedException(UserNotSavedException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DateNotParsedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleDateNotParsedException(DateNotParsedException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
