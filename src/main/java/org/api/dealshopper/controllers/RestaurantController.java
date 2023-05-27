package org.api.dealshopper.controllers;

import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.PaginatedRestaurantDTO;
import org.api.dealshopper.models.PaginatedRestaurantDTO2;
import org.api.dealshopper.models.RestaurantDTO;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.repositories.UserRepository;
import org.api.dealshopper.services.JwtService;
import org.api.dealshopper.services.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<SingleRestaurantResponse> getRestaurantById(@PathVariable Integer id, @RequestParam(required = false) String platformName) {
        SingleRestaurantResponse response;
        Restaurant restaurant = restaurantService.findRestaurantById(id);

        if (platformName == null) {
            //System.out.println(restaurantService.getBestPlatform(restaurant));
            response = restaurantService.getRestaurant(restaurant, restaurantService.getBestPlatform(restaurant));
        } else {
            response = restaurantService.getRestaurant(restaurant, platformName);
        }

        if (response == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRestaurants(
            @RequestParam(required = false, defaultValue = "0") Double minRating,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "9999") Double maxPrice,
            @RequestParam(required = false, defaultValue = "0") Integer minDeliveryTime,
            @RequestParam(required = false, defaultValue = "9999") Integer maxDeliveryTime,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer restaurantsPerPage,
            @RequestHeader(required = false, value = "Authorization", defaultValue = "Bearer defaultValue") String authorizationHeader) {

        String jwt = authorizationHeader.substring("Bearer ".length()).trim();
        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (jwt.equals("defaultValue")) {
            PaginatedRestaurantDTO2 restaurants = restaurantService.findAllRestaurants(minRating, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime, pageNumber, restaurantsPerPage);
            return ResponseEntity.ok(restaurants);
        } else {
            System.out.println("AICI " + jwt);
            if (jwt.equals("defaultValue")) System.out.println("DAA");
            else System.out.println("NUU");
            if (jwtService.authorizeToken(jwt)) {
                Integer userId = null;
                try {
                    userId = jwtService.extractUserId(jwt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                PaginatedRestaurantDTO restaurants = restaurantService.findAllRestaurantsWithFavorites(minRating, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime, pageNumber, restaurantsPerPage, userId);
                return ResponseEntity.ok(restaurants);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}