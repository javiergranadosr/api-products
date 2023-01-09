package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "departments")
@Data
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_deparment", length = 20, nullable = false)
    private String keyDeparment;

    @Column(length = 200, nullable = false)
    private String name;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"department", "hibernateLazyInitializer", "handler"})
    private List<Category> categories;
}
