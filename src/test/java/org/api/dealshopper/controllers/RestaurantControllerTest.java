package org.api.dealshopper.controllers;

import org.api.dealshopper.controllers.RestaurantController;
import org.api.dealshopper.controllers.UserController;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.models.SingleRestaurantResponse;
import org.api.dealshopper.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantControllerTest
{
    @Mock
    RestaurantService restaurantService;

    @InjectMocks
    RestaurantController restaurantController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        //restaurantController = new RestaurantController(restaurantService);
    }

    @Test
    void testGetRestaurantByIdWithoutPlatform() throws Exception
    {
        String restaurantName = "Restaurant Name";
        String platformName = "Best Platform";
        Restaurant restaurant = new Restaurant(1, restaurantName, "address", "phone", "img.jpg");

        when(restaurantService.findRestaurantById(1)).thenReturn(restaurant);
        when(restaurantService.getBestPlatform(any(Restaurant.class))).thenReturn(platformName);
        when(restaurantService.getRestaurant(restaurant, platformName)).thenReturn(new SingleRestaurantResponse(restaurantName, platformName, null));

        ResponseEntity<SingleRestaurantResponse> response = restaurantController.getRestaurantById(1, null);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof SingleRestaurantResponse);
        assertEquals(response.getBody().getName(), restaurantName);
        assertEquals(response.getBody().getPlatformName(), platformName);

        verify(restaurantService).findRestaurantById(1);
        verify(restaurantService).getBestPlatform(any(Restaurant.class));
        verify(restaurantService).getRestaurant(restaurant, platformName);
    }

    @Test
    void testGetRestaurantByIdWithPlatform() throws Exception
    {
        String restaurantName = "Restaurant Name";
        String platformName = "Best Platform";
        Restaurant restaurant = new Restaurant(1, restaurantName, "address", "phone", "img.jpg");

        when(restaurantService.findRestaurantById(1)).thenReturn(restaurant);
        when(restaurantService.getRestaurant(restaurant, platformName)).thenReturn(new SingleRestaurantResponse(restaurantName, platformName, null));

        ResponseEntity<SingleRestaurantResponse> response = restaurantController.getRestaurantById(1, platformName);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof SingleRestaurantResponse);
        assertEquals(response.getBody().getName(), restaurantName);
        assertEquals(response.getBody().getPlatformName(), platformName);

        verify(restaurantService).findRestaurantById(1);
        verify(restaurantService).getRestaurant(restaurant, platformName);
    }

    @Test
    void testGetNonexistentRestaurant() throws Exception
    {
        when(restaurantService.findRestaurantById(100)).thenReturn(null);

        ResponseEntity<SingleRestaurantResponse> response = restaurantController.getRestaurantById(100, null);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

        verify(restaurantService).findRestaurantById(100);
    }
}