package com.example.demo.controllers;

import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.services.IDepartmentService;
import com.example.demo.utils.ResponseSuccess;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/departments")
@AllArgsConstructor
public class DepartmentController {

    private final IDepartmentService service;

    @GetMapping
    ResponseEntity<Page<Department>> findAll(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy
    ) {
        return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> create(@Valid @RequestBody DepartmentDTO departmentDTO, BindingResult results) {
        // Verificamos si tenemos errores en la validacion de campos, caso contrario guardamos departamento
        if (results.hasErrors()) {
            Map<String, Object> errorMap = new HashMap<>();
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(err -> "Field " + err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            errorMap.put("code", HttpStatus.BAD_REQUEST.value());
            errorMap.put("errors", errors);
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        this.service.create(departmentDTO);
        ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Department created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
