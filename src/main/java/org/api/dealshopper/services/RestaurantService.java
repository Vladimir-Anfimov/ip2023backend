package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.DeliveryInfo;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.Category;
import org.api.dealshopper.models.ProductDto;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.repositories.ProductRepository;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService
{
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAll();
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

    public String getBestPlatform(Restaurant restaurant)
    {
        try
        {
            return restaurant
                    .getDeliveryInfoList()
                    .stream()
                    .min(Comparator.comparing(DeliveryInfo::getEfficiency))
                    .get()
                    .getId()
                    .getDeliveryPlatform();
        }
        catch (Exception e)
        {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }
}
