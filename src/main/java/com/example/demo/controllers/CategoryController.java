package com.example.demo.controllers;

import com.example.demo.exceptions.ApiError;
import com.example.demo.models.Category;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.services.ICategoryService;
import com.example.demo.utils.ResponseSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
@Api(tags = "CategoryController")
public class CategoryController {

    private final ICategoryService service;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all categories. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Page.class),
    })
    public ResponseEntity<Page<Category>> findAll(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy

    ) {
        return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Category.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    public ResponseEntity<Category> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.service.findById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new category.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ResponseSuccess.class),
    })
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult results) {
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
            this.service.create(categoryDTO);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Category created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

}
