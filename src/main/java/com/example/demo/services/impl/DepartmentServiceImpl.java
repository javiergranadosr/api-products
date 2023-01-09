package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorDataAccessException;
import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.repositories.DeparmentRepository;
import com.example.demo.services.IDepartmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DeparmentRepository departmentRepository;

    @Override
    public Page<Department> findAll(int page, int size, String orderBy) {
        return null;
    }

    @Override
    public Optional<Department> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Department create(DepartmentDTO departmentDTO) {
        log.info("Init create()");
        try{
            log.info("Success in create");
            Department data = new Department(null,departmentDTO.getKeyDeparment(), departmentDTO.getName(), null);
            return this.departmentRepository.save(data);
        }catch (DataAccessException e) {
            log.info("Failed in create");
            log.info(e.getMessage());
            throw new ErrorDataAccessException("Error in create department...");
        }
    }

    @Override
    public Department update(Department department, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
