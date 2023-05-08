package org.api.dealshopper.services;

import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductsByRestaurantId(Integer restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    /*
    public List<Product> getProductsByRestaurantId(Integer restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }

    public Product saveOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

     */
}
