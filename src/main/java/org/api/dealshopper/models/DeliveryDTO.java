package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private Integer deliveryCost;
    private Integer deliveryTime;
    private String deliveryPlatform;

    public DeliveryDTO(Integer deliveryCost, String deliveryPlatform, Integer deliveryTime) {
        this.deliveryCost = deliveryCost;
        this.deliveryPlatform = deliveryPlatform;
        this.deliveryTime = deliveryTime;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public double doEfficiency() {
        return ((double) getDeliveryTime() + (double) getDeliveryCost()) / 2;
    }
}
