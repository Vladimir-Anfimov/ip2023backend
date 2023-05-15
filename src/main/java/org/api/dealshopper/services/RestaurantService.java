package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.DeliveryInfo;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.*;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.springframework.data.domain.*;
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

    public PaginatedRestaurantDTO findAllRestaurants(Double minRating, Double minPrice, Double maxPrice,
                                                     Integer minDeliveryTime, Integer maxDeliveryTime, Integer pageNumber,
                                                     Integer restaurantsPerPage) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id")
                .and(Sort.by(Sort.Direction.ASC, "rating"))
                .and(Sort.by(Sort.Direction.ASC, "name"));

        Pageable pageRequest = PageRequest.of(pageNumber - 1, restaurantsPerPage, sort);
        List<Restaurant> allRestaurants = restaurantRepository.findByRatingGreaterThanEqualAndDeliveryInfoListDeliveryCostGreaterThanEqualAndDeliveryInfoListDeliveryCostLessThanEqualAndDeliveryInfoListDeliveryTimeGreaterThanEqualAndDeliveryInfoListDeliveryTimeLessThanEqual(
                minRating, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime);
        int totalRestaurants = allRestaurants.size();
        //System.out.println(totalRestaurants);
        int totalPages = (totalRestaurants + restaurantsPerPage - 1) / restaurantsPerPage;
        if (pageNumber > totalPages || pageNumber < 1) {
            return new PaginatedRestaurantDTO(new ArrayList<>(), pageNumber, restaurantsPerPage, totalPages, false, false);
        }

        int startIndex = (pageNumber - 1) * restaurantsPerPage;
        int endIndex = Math.min(startIndex + restaurantsPerPage, totalRestaurants);
        List<Restaurant> restaurantsForPage = allRestaurants.subList(startIndex, endIndex);
        List<RestaurantDTO> list = restaurantsForPage.stream()
                .map(restaurant -> new RestaurantDTO(restaurant, restaurant.getDeliveryInfoList(),
                        minPrice, maxPrice, minDeliveryTime, maxDeliveryTime)).toList();
        boolean hasNext = pageNumber < totalPages;
        boolean hasPrevious = pageNumber > 1;
        return new PaginatedRestaurantDTO(list, pageNumber, restaurantsPerPage, totalPages, hasNext, hasPrevious);
    }


    public Restaurant findRestaurantById(Integer id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElse(null);
    }

    public SingleRestaurantResponse getRestaurant(Restaurant restaurant, String platformName) {
        List<Category> categories = new ArrayList<>();
        List<String> categoryNames;

        if (platformName == null) return null;

        if (restaurant != null) {
            categoryNames = restaurant.getMenu().stream().filter(p -> p.getDeliveryPlatform().equals(platformName))
                    .map(Product::getCategory).distinct().toList();

            for (var categoryName : categoryNames) {
                categories.add(new Category(categoryName,
                        restaurant
                                .getMenu()
                                .stream()
                                .filter(
                                        product -> product
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
        } else return null;
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