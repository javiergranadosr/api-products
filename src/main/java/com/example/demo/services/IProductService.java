package com.example.demo.services;

import com.example.demo.models.Product;
import com.example.demo.models.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface IProductService {
    Page<Product> findAll(int page, int size, String orderBy);
    Page<Product> findByCategoryId(Long categoryId, int page, int size, String orderBy);
    Product findById(Long id);
    Product create(ProductDTO productDTO);
    Product update(ProductDTO productDTO, Long id);
    void delete(Long id);

}
