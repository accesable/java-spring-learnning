package com.example.learningjava.controllers;

import com.example.learningjava.dtos.ProductDTO;

import com.example.learningjava.exceptions.ProductNotFoundException;
import com.example.learningjava.models.Product;
import com.example.learningjava.service.ProductService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    public List<Product> getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return productService.getAllProducts();
    }
    // Create a new product
    @PostMapping
    public Product createProduct(@RequestBody @Valid ProductDTO productRequest) {
        // Convert ProductRequestDTO to Product entity
        Product product = new Product();
        logger.info("Creating product with name " + productRequest.getName());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
        return productService.createProduct(product);
    }

    // Get a single product by ID
//    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }
        return product.get();
    }



    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}