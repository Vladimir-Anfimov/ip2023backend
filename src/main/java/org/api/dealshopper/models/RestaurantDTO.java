package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.domain.DeliveryInfo;
import org.api.dealshopper.domain.Restaurant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Integer id;
    private String name;

    private String address;

    private String phone;

    private String image;
    private double rating;

    private double deliveryCost;
    private double deliveryTime;
    private String deliveryPlatform;
    private boolean favourite;

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }



    public RestaurantDTO(Restaurant restaurant, List<DeliveryInfo> deliveryInfoList, Double minPrice, Double maxPrice,
                         Integer minDeliveryTime, Integer maxDeliveryTime) {
        this.phone = restaurant.getPhone();
        this.name = restaurant.getName();
        this.image = restaurant.getImage();
        this.rating = restaurant.getRating();
        this.address = restaurant.getAddress();
        List<DeliveryDTO> list = deliveryInfoList.stream()
                .filter(deliveryInfo -> deliveryInfo.getDeliveryCost() != null
                        && deliveryInfo.getDeliveryTime() != null
                        && deliveryInfo.getDeliveryCost() >= minPrice
                        && deliveryInfo.getDeliveryCost() <= maxPrice
                        && deliveryInfo.getDeliveryTime() >= minDeliveryTime
                        && deliveryInfo.getDeliveryTime() <= maxDeliveryTime)
                .map(deliveryInfo -> new DeliveryDTO(deliveryInfo.getDeliveryCost(), deliveryInfo.getId().getDeliveryPlatform(), deliveryInfo.getDeliveryTime()))
                .sorted(Comparator.comparingDouble(DeliveryDTO::doEfficiency))
                .collect(Collectors.toList());

        this.deliveryCost = list.get(0).getDeliveryCost();
        this.deliveryTime = list.get(0).getDeliveryTime();
        this.deliveryPlatform = list.get(0).getDeliveryPlatform();
        this.id = restaurant.getId();
    }


    public RestaurantDTO(Restaurant restaurant) {
        id = restaurant.getId();
        name = restaurant.getName();
        image = restaurant.getImage();
        phone = restaurant.getPhone();
        address = restaurant.getAddress();
        rating=restaurant.getRating();

        List<DeliveryDTO> list = restaurant.getDeliveryInfoList().stream()
                .map(deliveryInfo -> new DeliveryDTO(deliveryInfo.getDeliveryCost(), deliveryInfo.getId().getDeliveryPlatform(), deliveryInfo.getDeliveryTime()))
                .sorted(Comparator.comparingDouble(DeliveryDTO::doEfficiency))
                .collect(Collectors.toList());

        this.deliveryCost = list.get(0).getDeliveryCost();
        this.deliveryTime = list.get(0).getDeliveryTime();
        this.deliveryPlatform = list.get(0).getDeliveryPlatform();
        this.favourite=true;
    }
}
