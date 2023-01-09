package com.example.demo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class DepartmentDTO {
    @NotEmpty( message = "keyDeparment is required")
    @Size(min = 4, max = 20)
    private String keyDeparment;

    @NotEmpty(message = "Name is required")
    @Size(min = 10, max = 200)
    private String name;
}
