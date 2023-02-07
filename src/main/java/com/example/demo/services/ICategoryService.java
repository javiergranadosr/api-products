package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.utils.ListCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    Page<Category> findAll(int page, int size, String orderBy);
    Page<Category> findByDepartmentId(Long departmentId, int page, int size, String orderBy);
    Category findById(Long id);
    Category create(CategoryDTO categoryDTO);
    Category update(CategoryDTO categoryDTO, Long id);
    void delete(Long id);
    List<ListCategory> getAllCategories(Long id);
}
