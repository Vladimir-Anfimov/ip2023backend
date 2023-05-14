package org.api.dealshopper.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoId implements Serializable {

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "delivery_platform")
    private String deliveryPlatform;

    public String getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setDeliveryPlatform(String deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }
}
