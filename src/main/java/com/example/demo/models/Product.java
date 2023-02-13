package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String brand;

    @Column(length = 50, nullable = false)
    private String model;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double discount;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"products", "hibernateLazyInitializer", "handler"})
    private Category category;

}
