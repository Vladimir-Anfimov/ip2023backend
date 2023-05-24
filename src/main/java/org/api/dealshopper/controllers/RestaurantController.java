package org.api.dealshopper.controllers;

import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.PaginatedRestaurantDTO;
import org.api.dealshopper.models.RestaurantDTO;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.services.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

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
    public PaginatedRestaurantDTO getAllRestaurants(
            @RequestParam(required = false, defaultValue = "0") Double minRating,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "9999") Double maxPrice,
            @RequestParam(required = false, defaultValue = "0") Integer minDeliveryTime,
            @RequestParam(required = false, defaultValue = "9999") Integer maxDeliveryTime,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer restaurantsPerPage) {
        PaginatedRestaurantDTO restaurants = restaurantService.findAllRestaurants(minRating,minPrice,maxPrice,minDeliveryTime,maxDeliveryTime,pageNumber,restaurantsPerPage);

        return restaurants;
    }

}