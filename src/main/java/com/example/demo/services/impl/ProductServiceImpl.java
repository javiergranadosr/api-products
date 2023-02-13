package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorNotFound;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.models.dto.ProductDTO;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.IProductService;
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
public class ProductServiceImpl implements IProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    /**
     * Obtener listado de todos los productos
     *
     * @param page
     * @param size
     * @param orderBy
     * @return
     */
    @Override
    public Page<Product> findAll(int page, int size, String orderBy) {
        log.info("Init findAll products");
        return this.productRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, orderBy)));
    }

    /**
     * Buscar listado de productos por categoria
     * @param categoryId
     * @param page
     * @param size
     * @param orderBy
     * @return
     */
    @Override
    public Page<Product> findByCategoryId(Long categoryId, int page, int size, String orderBy) {
        log.info("Init findByCategoryId products");
        return this.productRepository.findByCategoryId(categoryId, PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, orderBy)));
    }

    /**
     * Buscar producto por ID
     *
     * @param id
     * @return
     */
    @Override
    public Product findById(Long id) {
        log.info("Init findById product");
        return this.productRepository.findById(id).orElseThrow(() -> new ErrorNotFound("Product not found"));
    }

    /**
     * Crear un nuevo producto
     *
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
                        null,
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

    /**
     * Actualizar producto
     *
     * @param productDTO
     * @param id
     * @return
     */
    @Override
    public Product update(ProductDTO productDTO, Long id) {
        log.info("Init update product");
        try {
            Long categoryId = Long.parseLong(productDTO.getCategoryId());
            Optional<Category> category = this.categoryRepository.findById(categoryId);
            if (category.isPresent()) {
                Optional<Product> product = this.productRepository.findById(id);
                if (product.isPresent()) {
                    product.get().setName(productDTO.getName());
                    product.get().setBrand(productDTO.getBrand());
                    product.get().setDiscount(productDTO.getDiscount());
                    product.get().setModel(productDTO.getModel());
                    product.get().setPrice(productDTO.getPrice());
                    product.get().setCategory(category.get());
                    return this.productRepository.save(product.get());
                } else {
                    log.error("Product not found");
                    throw new ErrorNotFound("Product not found.");
                }
            } else {
                log.error("Category not found");
                throw new ErrorNotFound("Category not found.");
            }
        } catch (NumberFormatException e) {
            log.error("Invalid category, must be number.");
            throw new NumberFormatException("Invalid category, must be number.");
        }
    }

    /**
     * Eliminar producto por Id
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.info("Init delete producto.");
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            this.productRepository.delete(product.get());
        } else {
            log.error("Product not found");
            throw new ErrorNotFound("Product not found.");
        }
    }

    /**
     * Se crea servicio para guardar fotos en productos
     * @param productId
     * @param filename
     * @return
     */
    @Override
    public Product saveFile(Long productId, String filename) {
        log.info("Init saveFile() in products");
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ErrorNotFound("Product not found");
        }
        product.get().setImage(filename);
        return this.productRepository.save(product.get());
    }
}
