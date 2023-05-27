package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.DeliveryInfo;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.*;
import org.api.dealshopper.repositories.DeliveryInfoRepository;
import org.api.dealshopper.repositories.ProductRepository;
import org.api.dealshopper.repositories.RestaurantRepository;
import org.api.dealshopper.repositories.UserRepository;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryInfoRepository deliveryInfoRepository;
    private final JwtService jwtService;

    public PaginatedRestaurantDTO2 findAllRestaurants(Double minRating, Double minPrice, Double maxPrice,
                                                     Integer minDeliveryTime, Integer maxDeliveryTime,
                                                     Integer pageNumber, Integer restaurantsPerPage) {

        Pageable pageable = PageRequest.of(pageNumber, restaurantsPerPage);
        Page<Restaurant> pageResult = restaurantRepository.findAllWithPagination(minRating, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime, pageable);

        List<RestaurantDTO2> restaurantDTOList = new ArrayList<>();

        for (Restaurant restaurant : pageResult.getContent()) {
            List<DeliveryInfo> deliveryInfoList = restaurant.getDeliveryInfoList();
            RestaurantDTO2 restaurantDTO = new RestaurantDTO2(restaurant, deliveryInfoList, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime);
            restaurantDTOList.add(restaurantDTO);
        }

        int currentPageNumber = pageResult.getNumber();
        int pageSize = pageResult.getSize();
        int totalPages = pageResult.getTotalPages();
        boolean hasNextPage = pageResult.hasNext();
        boolean hasPreviousPage = pageResult.hasPrevious();

        PaginatedRestaurantDTO2 paginatedRestaurantDTO = new PaginatedRestaurantDTO2(restaurantDTOList, currentPageNumber, pageSize, totalPages);
        paginatedRestaurantDTO.setHasNext(hasNextPage);
        paginatedRestaurantDTO.setHasPrevious(hasPreviousPage);

        return paginatedRestaurantDTO;
    }

    public PaginatedRestaurantDTO findAllRestaurantsWithFavorites(
            Double minRating, Double minPrice, Double maxPrice,
            Integer minDeliveryTime, Integer maxDeliveryTime,
            Integer pageNumber, Integer restaurantsPerPage,
            Integer userId
    ) {

        Pageable pageable = PageRequest.of(pageNumber, restaurantsPerPage);
        Page<Restaurant> pageResult = restaurantRepository.findAllWithPagination(minRating, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime, pageable);

        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();

        for (Restaurant restaurant : pageResult.getContent()) {
            List<DeliveryInfo> deliveryInfoList = restaurant.getDeliveryInfoList();
            RestaurantDTO restaurantDTO = new RestaurantDTO(restaurant, deliveryInfoList, minPrice, maxPrice, minDeliveryTime, maxDeliveryTime);

            // vad daca userul meu are restaurante favorite
            boolean isFavorite = restaurant.getRestaurantFans()
                    .stream()
                    .map(User::getId)
                    .anyMatch(id -> id.equals(userId));
            restaurantDTO.setFavourite(isFavorite);

            restaurantDTOList.add(restaurantDTO);
        }

        int currentPageNumber = pageResult.getNumber();
        int pageSize = pageResult.getSize();
        int totalPages = pageResult.getTotalPages();
        boolean hasNextPage = pageResult.hasNext();
        boolean hasPreviousPage = pageResult.hasPrevious();

        PaginatedRestaurantDTO paginatedRestaurantDTO = new PaginatedRestaurantDTO(restaurantDTOList, currentPageNumber, pageSize, totalPages);
        paginatedRestaurantDTO.setHasNext(hasNextPage);
        paginatedRestaurantDTO.setHasPrevious(hasPreviousPage);

        return paginatedRestaurantDTO;

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

            categoryNames = productRepository.getCategoriesByRestaurantIdAndDeliveryPlatform(restaurant.getId(), platformName);

            for (var categoryName : categoryNames) {
                categories.add(new Category(categoryName,
                        productRepository
                                .findByRestaurantIdAndCategoryAndDeliveryPlatform(restaurant.getId(), categoryName, platformName)
                                .stream()
                                .map(ProductDto::new)
                                .toList()
                ));
            }

            return new SingleRestaurantResponse(restaurant.getName(), platformName, categories);
        } else return null;
    }


    public String getBestPlatform(Restaurant restaurant) {
        try {
            return deliveryInfoRepository.getBestDeliveryPlatformByRestaurantId(restaurant.getId()).orElse(null);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }
}