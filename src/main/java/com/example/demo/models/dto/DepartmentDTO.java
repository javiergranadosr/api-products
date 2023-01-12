package com.example.demo.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DepartmentDTO {
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 20)
    private String keyDepartment;

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 200)
    private String name;
}
