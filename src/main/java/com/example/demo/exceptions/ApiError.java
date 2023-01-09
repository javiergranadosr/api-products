package com.example.demo.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime date;
    private int code;
    private String messageCode;
    private String message;
    private String path;
}
