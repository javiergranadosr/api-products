package com.example.demo.exceptions;

import org.springframework.dao.DataAccessException;

public class ErrorDataAccessException extends DataAccessException {
    public ErrorDataAccessException(String msg) {
        super(msg);
    }
}
