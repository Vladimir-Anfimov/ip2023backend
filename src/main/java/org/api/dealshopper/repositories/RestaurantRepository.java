package org.api.dealshopper.repositories;

import org.api.dealshopper.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, Integer> {
    Optional<Restaurant> findById(Integer id);

    Page<Restaurant> findAll(Pageable pageable);

    List<Restaurant> findByRatingGreaterThanEqualAndDeliveryInfoListDeliveryCostGreaterThanEqualAndDeliveryInfoListDeliveryCostLessThanEqualAndDeliveryInfoListDeliveryTimeGreaterThanEqualAndDeliveryInfoListDeliveryTimeLessThanEqual(
            double rating,
            double minPrice,
            double maxPrice,
            int minDeliveryTime,
            int maxDeliveryTime
    );


}
