package com.example.demo.exceptions;

public class ErrorNotFound extends RuntimeException{
    public ErrorNotFound(String message) {
        super(message);
    }
}
