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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final CategoryRepository repository;
    private final DepartmentRepository departmentRepository;

    /**
     * Listado de categorias
     *
     * @param page    # de pagina
     * @param size    Cantidad de categorias a mostrar por pagina
     * @param orderBy Ordenar por
     * @return
     */
    @Override
    public Page<Category> findAll(int page, int size, String orderBy) {
        log.info("Init findAll categories");
        return this.repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, orderBy)));
    }

    /**
     * Buscar categoria por ID
     *
     * @param id
     * @return
     */
    @Override
    public Category findById(Long id) {
        log.info("Init findById category");
        return this.repository.findById(id).orElseThrow(() -> new ErrorNotFound("Category not found."));
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

    /**
     * Actualizar categoria
     *
     * @param categoryDTO
     * @param id
     * @return
     */
    @Override
    public Category update(CategoryDTO categoryDTO, Long id) {
        log.info("Init update category");
        Optional<Category> category = this.repository.findById(id);
        if (category.isPresent()) {
            try {
                Long departmentId = Long.parseLong(categoryDTO.getDepartmentId());
                Optional<Department> department = this.departmentRepository.findById(departmentId);

                if (department.isPresent()) {
                    Category categoryToPersist = category.get();
                    categoryToPersist.setName(categoryDTO.getName());
                    categoryToPersist.setDepartment(department.get());
                    return this.repository.save(categoryToPersist);
                } else {
                    log.error("Department not found.");
                    throw new ErrorNotFound("Department not found.");
                }
            } catch (NumberFormatException e) {
                log.error("Invalid department, must be number.");
                throw new NumberFormatException("Invalid department, must be number.");
            }
        } else {
            log.error("Error update category.");
            log.error("Category not found.");
            throw new ErrorNotFound("Category not found.");
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Init delete category");
        Optional<Category> category = this.repository.findById(id);
        if (category.isPresent()) {
            this.repository.delete(category.get());
        }else {
            log.error("Error deleted category.");
            log.error("Category not found.");
            throw new ErrorNotFound("Category not found.");
        }
    }
}
