package org.api.dealshopper.services;

import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.repositories.DeliveryInfoRepository;
import org.api.dealshopper.repositories.ProductRepository;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DeliveryInfoRepository deliveryInfoRepository;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.initMocks(this);
        restaurantService = new RestaurantService(restaurantRepository, productRepository, deliveryInfoRepository);
    }

    @Test
    void findRestaurantById()
    {
        Integer id = 1;
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.findRestaurantById(id);

        assertEquals(restaurant, result);

        verify(restaurantRepository).findById(id);
    }

    @Test
    void findRestaurantByIdNonExistent()
    {
        Integer id = 1;
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        Restaurant result = restaurantService.findRestaurantById(id);

        assertNull(result);

        verify(restaurantRepository).findById(id);
    }

    @Test
    void getRestaurant()
    {
        String platformName = "Platform1";
        Restaurant restaurant = new Restaurant();
        String categoryName = "category";

        when(productRepository.getCategoriesByRestaurantIdAndDeliveryPlatform(restaurant.getId(), platformName)).thenReturn(List.of(categoryName, "other"));

        SingleRestaurantResponse result = restaurantService.getRestaurant(restaurant, platformName);

        assertEquals(result.getPlatformName(), platformName);
        assertEquals(result.getCategories().size(), 2);

        verify(productRepository).getCategoriesByRestaurantIdAndDeliveryPlatform(restaurant.getId(), platformName);
        verify(productRepository).findByRestaurantIdAndCategoryAndDeliveryPlatform(restaurant.getId(), categoryName, platformName);
    }

    @Test
    void getRestaurantNullRestaurant()
    {
        SingleRestaurantResponse result = restaurantService.getRestaurant(null, "platform");

        assertNull(result);
    }

    @Test
    void getRestaurantNullPlatform()
    {
        SingleRestaurantResponse result = restaurantService.getRestaurant(new Restaurant(), null);

        assertNull(result);
    }

    @Test
    void getBestPlatform()
    {
        Integer restaurantId = 1;
        String platform = "Best Platform";

        Restaurant restaurant = new Restaurant(restaurantId, "name", "address", "phone", "image");

        when(deliveryInfoRepository.getBestDeliveryPlatformByRestaurantId(restaurantId)).thenReturn(Optional.of(platform));

        String bestPlatform = restaurantService.getBestPlatform(restaurant);

        assertEquals(bestPlatform, platform);
    }

    @Test
    void getBestPlatformException()
    {
        Integer restaurantId = 1;

        Restaurant restaurant = new Restaurant(restaurantId, "name", "address", "phone", "image");

        when(deliveryInfoRepository.getBestDeliveryPlatformByRestaurantId(restaurantId)).thenThrow(new RuntimeException());

        String bestPlatform = restaurantService.getBestPlatform(restaurant);

        assertNull(bestPlatform);
    }
}