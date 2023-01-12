package com.example.demo.controllers;

import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.services.IDepartmentService;
import com.example.demo.utils.ResponseSuccess;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/departments")
@AllArgsConstructor
public class DepartmentController {

    private final IDepartmentService service;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<Department>> findAll(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy
    ) {
        return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.service.findById(id), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
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

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(
            @Valid @RequestBody DepartmentDTO departmentDTO,
            BindingResult results,
            @PathVariable("id") Long id
    ) {
        if (results.hasErrors()) {
            Map<String, Object> errorMap = new HashMap<>();
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(err -> "Field " + err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            errorMap.put("code", HttpStatus.BAD_REQUEST.value());
            errorMap.put("errors", errors);
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        } else {
            this.service.update(departmentDTO, id);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Department updated successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
