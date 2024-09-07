# Java Spring Boot Revised
## Caching
Enable Caching in Spring Boot.By default Spring will use in memory Caching named `concurrentMap` you can use `Redis` for example
```java
@EnableCaching
public class LearningJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningJavaApplication.class, args);
	}

}
```
```java
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
        /* logic*/
    }
    // Evicts the specific "product" cache entry when a product is deleted
    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(Long id) {
        /* logic*/
    }
```# java-spring-learnning
