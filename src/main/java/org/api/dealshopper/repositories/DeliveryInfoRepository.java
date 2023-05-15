package org.api.dealshopper.repositories;

import org.api.dealshopper.domain.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer>
{
    @Query(value = "select delivery_platform from delivery_info where restaurant_id = :restaurantId order by (delivery_time + delivery_cost)/2 limit 1;", nativeQuery = true)
    Optional<String> getBestDeliveryPlatformByRestaurantId(Integer restaurantId);
}
