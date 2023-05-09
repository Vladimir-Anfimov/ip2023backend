package org.api.dealshopper.repositories;

import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAll();

    Optional<Product> findById(Integer id);

    List<Product> findByRestaurantId(Integer restaurantId);

    List<Product> findByRestaurantIdAndCategoryAndDeliveryPlatform(Integer restaurantId, String category, String deliveryPlatform);
}
