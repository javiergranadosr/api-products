package com.example.demo.controllers;

import com.example.demo.models.dto.ProductDTO;
import com.example.demo.services.IProductService;
import com.example.demo.utils.ResponseSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/v1/products")
@AllArgsConstructor
@Api(tags = "ProductController")
public class ProductController {

    private final IProductService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new product.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ResponseSuccess.class),
    })
    public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO, BindingResult results) {
        if (results.hasErrors()){
            Map<String, Object> errorMap = new HashMap<>();
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(err -> "Field " + err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            errorMap.put("code", HttpStatus.BAD_REQUEST.value());
            errorMap.put("errors", errors);
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }else {
            this.service.create(productDTO);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Product created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
}
