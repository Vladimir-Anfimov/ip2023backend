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
public class DeliveryPriceId implements Serializable {

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "delivery_platform")
    private Integer deliveryPlatform;

}
