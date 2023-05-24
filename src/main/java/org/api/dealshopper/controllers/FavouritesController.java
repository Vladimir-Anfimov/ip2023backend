package org.api.dealshopper.controllers;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.FavouritesResponse;
import org.api.dealshopper.repositories.UserRepository;
import org.api.dealshopper.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouritesController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping(path="products")
    public ResponseEntity<?>  getAllProducts(@RequestParam String token) {
        try {
            Integer userId = jwtService.extractUserId(token);
            User user=userRepository.findById(userId).get();
            List<Product> products=user.getFavouriteProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(path="/restaurants")
    public ResponseEntity<?> getAllRestaurants(@RequestParam String token) {
        try {
            Integer userId = jwtService.extractUserId(token);
            User user=userRepository.findById(userId).get();
            List<Restaurant> restaurants=user.getFavouriteRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
