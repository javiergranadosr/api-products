package com.example.demo.controllers;

import com.example.demo.exceptions.ApiError;
import com.example.demo.models.Product;
import com.example.demo.models.dto.ProductDTO;
import com.example.demo.services.IProductService;
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
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET})
@AllArgsConstructor
@Api(tags = "ProductController")
public class ProductController {

    private final IProductService service;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Page.class),
    })
    public ResponseEntity<Page<Product>> findAll(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy
    ) {
        return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get product by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Product.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.service.findById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new product.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ResponseSuccess.class),
    })
    public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO, BindingResult results) {
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
            this.service.create(productDTO);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Product created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Update product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<?> update(
            @Valid @RequestBody ProductDTO productDTO,
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
            this.service.update(productDTO, id);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Product updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<ResponseSuccess> delete(@PathVariable("id") Long id) {
        this.service.delete(id);
        ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Product deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

