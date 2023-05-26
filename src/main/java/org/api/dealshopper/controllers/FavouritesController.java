package org.api.dealshopper.controllers;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.*;
import org.api.dealshopper.repositories.ProductRepository;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.api.dealshopper.repositories.UserRepository;
import org.api.dealshopper.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouritesController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<FavouritesResponse> getAllFavourites(@RequestParam String token) {
        try {
            Integer userId = jwtService.extractUserId(token);
            User user = userRepository.findById(userId).get();
            List<Product> products = user.getFavouriteProducts();
            List<Restaurant> restaurants = user.getFavouriteRestaurants();
            List<ProductDto> productDtos = products.stream().map(ProductDto::new).collect(Collectors.toList());
            List<RestaurantDTO> restaurantDTOs = restaurants.stream().map(RestaurantDTO::new).collect(Collectors.toList());
            FavouritesResponse response = new FavouritesResponse(productDtos, restaurantDTOs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


     @PostMapping(path="/product/state")
    public ResponseEntity<?> addOrRemoveProductFromFavourites(@RequestBody FavouriteProductRequest request,
                                                              @RequestHeader(value="Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            Integer userId = jwtService.extractUserId(token);
            User user = userRepository.findById(userId).get();
            Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            if (request.getAction().equals("add")) {
                user.getFavouriteProducts().add(product);
                System.out.println(user.getFavouriteProducts());
            } else if (request.getAction().equals("remove")) {
                user.getFavouriteProducts().remove(product);
            } else {
                throw new RuntimeException("Invalid action provided");
            }

            userRepository.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /*
    @PostMapping(path="/restaurant/state")
    public ResponseEntity<?> addOrRemoveRestaurantFromFavourites(@RequestBody FavouriteRestaurantRequest request,
                                                                 @RequestHeader(value="Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            Integer userId = jwtService.extractUserId(token);
            User user=userRepository.findById(userId).get();

            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> new RuntimeException("Restaurant not found"));

            if(request.getAction().equals("add")) {
                user.getFavouriteRestaurants().add(restaurant);
                System.out.println(user.getFavouriteRestaurants());
            } else if (request.getAction().equals("remove")) {
                user.getFavouriteRestaurants().remove(restaurant);
            } else {
                throw new RuntimeException("Invalid action provided");
            }

            userRepository.save(user);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
     */
}
