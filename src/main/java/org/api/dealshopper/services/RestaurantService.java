package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant findRestaurantById(Integer id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElse(null);
    }
}
