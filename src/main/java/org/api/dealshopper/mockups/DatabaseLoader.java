package org.api.dealshopper.mockups;

import org.api.dealshopper.domain.*;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.api.dealshopper.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * populates the db with mockup data
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public DatabaseLoader(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void run(String... args) throws Exception {

       
    }
}
