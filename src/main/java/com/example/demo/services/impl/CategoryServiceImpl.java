package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorNotFound;
import com.example.demo.models.Category;
import com.example.demo.models.Department;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.services.ICategoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final CategoryRepository repository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Page<Category> findAll(int page, int size, String orderBy) {
        return null;
    }

    @Override
    public Category findById(Long id) {
        return null;
    }

    /**
     * Crear una nueva categoria
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public Category create(CategoryDTO categoryDTO) {
        log.info("Init create category");
        try {
            Long departmentId = Long.parseLong(categoryDTO.getDepartmentId());
            Optional<Department> department = this.departmentRepository.findById(departmentId);
            if (department.isPresent()) {
                Category category = new Category(null, categoryDTO.getName(), department.get(), null);
                return this.repository.save(category);
            } else {
                log.error("Department not found");
                throw new ErrorNotFound("Department not found.");
            }
        } catch (NumberFormatException e) {
            log.error("Invalid department, must be number.");
            throw new NumberFormatException("Invalid department, must be number.");
        }
    }

    @Override
    public Category update(CategoryDTO categoryDTO, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
