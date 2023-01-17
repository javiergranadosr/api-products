package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorNotFound;
import com.example.demo.models.Category;
import com.example.demo.models.Department;
import com.example.demo.models.Product;
import com.example.demo.models.dto.ProductDTO;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.IProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    @Override
    public Page<Product> findAll(int page, int size, String orderBy) {
        return null;
    }

    @Override
    public Product findById(Long id) {
        return null;
    }

    /**
     * Crear un nuevo producto
     * @param productDTO
     * @return
     */
    @Override
    public Product create(ProductDTO productDTO) {
        log.info("Init create a product");
        try {
            Long categoryId = Long.parseLong(productDTO.getCategoryId());
            Optional<Category> category = this.categoryRepository.findById(categoryId);
            if (category.isPresent()) {
                Product product = new Product(
                        null,
                        productDTO.getName(),
                        productDTO.getBrand(),
                        productDTO.getModel(),
                        productDTO.getPrice(),
                        productDTO.getDiscount(),
                        category.get());
                return this.productRepository.save(product);
            } else {
                log.error("Category not found");
                throw new ErrorNotFound("Category not found.");
            }
        } catch (NumberFormatException e) {
            log.error("Invalid category, must be number.");
            throw new NumberFormatException("Invalid category, must be number.");
        }
    }

    @Override
    public Product update(ProductDTO productDTO, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
