package com.example.demo.services;

import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.utils.ListDepartment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDepartmentService {
    Page<Department> findAll(int page, int size, String orderBy);
    Department findById(Long id);
    Department create(DepartmentDTO departmentDTO);
    Department update(DepartmentDTO departmentDTO, Long id);
    void delete(Long id);
    List<ListDepartment> getAllDepartments();
    Department saveFile(Long departmentId, String filename);
}
