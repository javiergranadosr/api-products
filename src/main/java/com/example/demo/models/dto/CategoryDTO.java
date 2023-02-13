package com.example.demo.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryDTO {
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 200)
    private String name;

    private String image;

    @NotNull
    @NotEmpty
    private String departmentId;
}
