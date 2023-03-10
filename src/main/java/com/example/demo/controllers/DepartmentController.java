package com.example.demo.controllers;

import com.example.demo.exceptions.ApiError;
import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.services.IDepartmentService;
import com.example.demo.services.IUploadFileService;
import com.example.demo.services.impl.UploadFileServiceImpl;
import com.example.demo.utils.CFiles;
import com.example.demo.utils.ListDepartment;
import com.example.demo.utils.ResponseSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/departments")
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE
})
@AllArgsConstructor
@Api(tags = "DepartmentController")
public class DepartmentController {

    private final IDepartmentService service;
    private final IUploadFileService uploadFileService;
    private final CFiles files;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all departments.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Page.class),
    })
    ResponseEntity<Page<Department>> findAll(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "orderBy", defaultValue = "id") String orderBy
    ) {
        return new ResponseEntity<>(this.service.findAll(page, size, orderBy), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get department by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = Department.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class)
    })
    public ResponseEntity<Department> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.service.findById(id), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Create a new department.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ResponseSuccess.class),
    })
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
    @ApiOperation(value = "Update department.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
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

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Delete department.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<ResponseSuccess> delete(@PathVariable("id") Long id) {
        this.service.delete(id);
        ResponseSuccess response = new ResponseSuccess(HttpStatus.OK.value(), "Department deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Get all department for select.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListDepartment.class),
    })
    public ResponseEntity<List<ListDepartment>> getAllDepartments() {
        return new ResponseEntity<>(this.service.getAllDepartments(), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save file in department")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = ResponseSuccess.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
    })
    public ResponseEntity<ResponseSuccess> savePhoto(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {

        String filename = null;
        Department department = this.service.findById(id);
        if (!file.isEmpty()) {
            filename = this.uploadFileService.saveFile(file, UploadFileServiceImpl.IMAGES_DEPARTMENTS);
            if (department.getImage() != null && department.getImage().length() > 0) {
                this.uploadFileService.deleteFile(UploadFileServiceImpl.IMAGES_DEPARTMENTS, department.getImage());
            }
            this.service.saveFile(id, filename);
            ResponseSuccess response = new ResponseSuccess(HttpStatus.CREATED.value(), "Department photo created successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        ResponseSuccess response = new ResponseSuccess(HttpStatus.NOT_FOUND.value(), "Department photo is invalid.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/show-image/{filename:.+}") // Example [image.png, image.jpeg, image.gif, ...]
    @ApiOperation(value = "Show image department")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Resource.class)})
    public ResponseEntity<Resource> showPhoto(@PathVariable("filename") String filename) {
        Resource resource = this.files.getResource(UploadFileServiceImpl.IMAGES_DEPARTMENTS, filename);
        return new ResponseEntity<>(resource, this.files.getHeaders(resource), HttpStatus.OK);
    }
}
