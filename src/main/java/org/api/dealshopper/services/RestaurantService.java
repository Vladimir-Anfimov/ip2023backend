package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.DeliveryInfo;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.Category;
import org.api.dealshopper.models.ProductDto;
import org.api.dealshopper.models.RestaurantDTO;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.repositories.ProductRepository;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public List<RestaurantDTO> findAllRestaurants(Double minimumRating, Double minPrice, Double maxPrice,
                                                  Double latitude, Double longitude, Integer minDeliveryTime,
                                                  Integer maxDeliveryTime, Integer pageNumber, Integer restaurantsPerPage) {

        // iau toate restuarantele si le filtrez
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // Filter dupa minimum rating
        if (minimumRating != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getRating() >= minimumRating)
                    .collect(Collectors.toList());
        }
        // Sortare restaurante dupa rating sa apara in ordine desc
        restaurants.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));

        List<RestaurantDTO> list = restaurants.stream().map(restaurant -> new RestaurantDTO(restaurant, getBestDeliveryInfoForRestaurant(restaurant, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime))).toList();

        return list;
    }

    public String getBestDeliveryInfoForRestaurant(Restaurant restaurant, Double minPrice, Double maxPrice,
                                                   Integer minDeliveryTime, Integer maxDeliveryTime) {
        // toate info de livrare disponibile pt un restaurant
        List<DeliveryInfo> deliveryInfoList = restaurant.getDeliveryInfoList();

        // filtrez dupa minimum price
        if (minPrice != null) {
            deliveryInfoList = deliveryInfoList.stream()
                    .filter(d -> d.getDeliveryCost() >= minPrice)
                    .collect(Collectors.toList());
        }

        // filtrez maximum price
        if (maxPrice != null) {
            deliveryInfoList = deliveryInfoList.stream()
                    .filter(d -> d.getDeliveryCost() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // filtrez dupa minimum delivery time
        if (minDeliveryTime != null) {
            deliveryInfoList = deliveryInfoList.stream()
                    .filter(d -> d.getDeliveryTime() >= minDeliveryTime)
                    .collect(Collectors.toList());
        }

        // filtrez dupa maximum delivery time
        if (maxDeliveryTime != null) {
            deliveryInfoList = deliveryInfoList.stream()
                    .filter(d -> d.getDeliveryTime() <= maxDeliveryTime)
                    .collect(Collectors.toList());
        }

        // sortez dupa eficienta
        deliveryInfoList.sort(Comparator.comparingDouble(DeliveryInfo::getEfficiency));

        // returnez prima platforma pe care sa am cea mai buna eficenta la acel restaurant
        return deliveryInfoList.isEmpty() ? null : deliveryInfoList.get(0).getId().getDeliveryPlatform();
    }


    public Restaurant findRestaurantById(Integer id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElse(null);
    }

    public SingleRestaurantResponse getRestaurant(Restaurant restaurant, String platformName)
    {
        List<Category> categories = new ArrayList<>();
        List<String> categoryNames;

        if (platformName == null) return null;

        if (restaurant != null)
        {
            categoryNames = restaurant.getMenu().stream().filter(p -> p.getDeliveryPlatform().equals(platformName))
                            .map(Product::getCategory).distinct().toList();

            for (var categoryName : categoryNames)
            {
                categories.add(new Category(categoryName,
                        restaurant
                                .getMenu()
                                .stream()
                                .filter(
                                        product ->  product
                                                            .getCategory()
                                                            .equals(categoryName)
                                                    &&
                                                    product
                                                            .getDeliveryPlatform()
                                                            .equals(platformName)
                                )
                                .map(ProductDto::new)
                                .toList()
                        ));
            }

            return new SingleRestaurantResponse(restaurant.getName(), platformName, categories);
        }
        else return null;
    }


    public String getBestPlatform(Restaurant restaurant) {
        try {
            return restaurant
                    .getDeliveryInfoList()
                    .stream()
                    .min(Comparator.comparing(DeliveryInfo::getEfficiency))
                    .get()
                    .getId()
                    .getDeliveryPlatform();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }
}
