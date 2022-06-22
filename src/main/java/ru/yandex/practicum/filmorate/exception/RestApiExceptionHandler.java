package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e.getMessage(), status);
    }
}
