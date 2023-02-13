package com.example.demo.controllers;

import com.example.demo.exceptions.ApiError;
import com.example.demo.models.Category;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.services.ICategoryService;
import com.example.demo.services.IUploadFileService;
import com.example.demo.services.impl.UploadFileServiceImpl;
import com.example.demo.utils.CFiles;
import com.example.demo.utils.ListCategory;
import com.example.demo.utils.ResponseSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*", methods= {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE
})
@AllArgsConstructor
@Api(tags = "CategoryController")
public class CategoryController {

    private final ICategoryService service;
    private final IUploadFileService uploadFileService;
    private final CFiles files;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all categories. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Page.class),
    })
    public ResponseEntity<Page<Category>> findAll(
            @RequestParam(required = false, value = "departmentId") Long departmentId,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy

    ) {
        if (departmentId == null || departmentId == 0) {
            return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
        }
        return new ResponseEntity<>(this.service.findByDepartmentId(departmentId, page, size, orderBy), HttpStatus.OK);
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

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Update category.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<?> update(
            @Valid @RequestBody CategoryDTO categoryDTO,
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
            this.service.update(categoryDTO, id);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Category updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Delete category.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.service.delete(id);
        ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Category deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/all/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation( value = "Get all categories for select.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListCategory.class),
    })
    public ResponseEntity<List<ListCategory>> getAllCategories(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.service.getAllCategories(id), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save file in category")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<ResponseSuccess> savePhoto(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {

        String filename = null;
        Category category = this.service.findById(id);
        if (!file.isEmpty()) {
            filename = this.uploadFileService.saveFile(file, UploadFileServiceImpl.IMAGES_CATEGORIES);
            if (category.getImage() != null && category.getImage().length() > 0) {
                this.uploadFileService.deleteFile(UploadFileServiceImpl.IMAGES_CATEGORIES, category.getImage());
            }
            this.service.saveFile(id, filename);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Category photo created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        ResponseSuccess response = new ResponseSuccess(HttpStatus.NOT_FOUND.value(), "Category photo is invalid.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/show-image/{filename:.+}") // Example [image.png, image.jpeg, image.gif, ...]
    @ApiOperation(value = "Show image category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Resource.class)})
    public ResponseEntity<Resource> showPhoto(@PathVariable("filename") String filename) {
        Resource resource = this.files.getResource(UploadFileServiceImpl.IMAGES_CATEGORIES, filename);
        return new ResponseEntity<>(resource, this.files.getHeaders(resource), HttpStatus.OK);
    }
}
