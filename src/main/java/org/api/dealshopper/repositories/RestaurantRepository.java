package org.api.dealshopper.repositories;

import org.api.dealshopper.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, Integer> {
    Optional<Restaurant> findById(Integer id);

    @Query("SELECT r FROM Restaurant r JOIN r.deliveryInfoList di WHERE r.rating >= :rating AND di.deliveryCost >= :minPrice AND di.deliveryCost <= :maxPrice AND di.deliveryTime >= :minDeliveryTime AND di.deliveryTime <= :maxDeliveryTime")
    Page<Restaurant> findAllWithPagination(double rating, double minPrice, double maxPrice, int minDeliveryTime, int maxDeliveryTime, Pageable pageable);

    Page<Restaurant> findAll(Pageable pageable);

}
