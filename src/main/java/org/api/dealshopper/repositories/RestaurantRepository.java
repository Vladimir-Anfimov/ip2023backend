package org.api.dealshopper.repositories;

import org.api.dealshopper.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    Optional<Restaurant> findById(Integer id);

    List<Restaurant> findAll();

}
