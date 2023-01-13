package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.models.dto.CategoryDTO;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    Page<Category> findAll(int page, int size, String orderBy);
    Category findById(Long id);
    Category create(CategoryDTO categoryDTO);
    Category update(CategoryDTO categoryDTO, Long id);
    void delete(Long id);
}
