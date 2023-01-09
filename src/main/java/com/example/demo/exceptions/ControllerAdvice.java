package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = ErrorDataAccessException.class)
    public ResponseEntity<ApiError> dataExceptionHandler(ErrorDataAccessException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setDate(LocalDateTime.now());
        apiError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setMessageCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());
        return new ResponseEntity<>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
