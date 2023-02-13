package com.example.demo.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProductDTO {
    @NotNull
    @NotEmpty
    @Size(min = 5, max = 200)
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 200)
    private String brand;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 50)
    private String model;

    @NotNull
    private double price;

    @NotNull
    private double discount;

    private String image;

    @NotNull
    @NotEmpty
    private String categoryId;
}
