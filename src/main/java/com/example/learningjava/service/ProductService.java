package com.example.learningjava.service;

import com.example.learningjava.exceptions.ProductNotFoundException;
import com.example.learningjava.models.Product;
import com.example.learningjava.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Cacheable(value = "product", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(String.format("Product ID : %d not found", id))
        ));
    }
    // Evicts the entire "products" cache when a new product is created
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    // Evicts the entire "products" cache when a product is updated
    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    return productRepository.save(product);
                }).orElseThrow(() -> new RuntimeException("Product not found"));
    }
    // Evicts the specific "product" cache entry when a product is deleted
    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}