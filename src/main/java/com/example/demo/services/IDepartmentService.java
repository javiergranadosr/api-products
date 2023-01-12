package com.example.demo.services;

import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IDepartmentService {
    Page<Department> findAll(int page, int size, String orderBy);
    Department findById(Long id);
    Department create(DepartmentDTO departmentDTO);
    Department update(Department department, Long id);
    void delete(Long id);
}
