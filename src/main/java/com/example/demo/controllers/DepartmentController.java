package com.example.demo.controllers;

import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.services.IDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(service.create(departmentDTO), HttpStatus.CREATED);
    }

}
