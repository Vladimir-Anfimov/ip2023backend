package org.api.dealshopper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "delivery_prices")
public class DeliveryPrice {

    @EmbeddedId
    private DeliveryPriceId id;

    @Column(name = "delivery_cost")
    private Integer deliveryCost;


    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;

    public DeliveryPrice() {}

    public DeliveryPrice(DeliveryPriceId id, Integer deliveryCost) {
        this.id = id;
        this.deliveryCost=deliveryCost;
    }

    public DeliveryPriceId getId() {
        return id;
    }

    public void setId(DeliveryPriceId id) {
        this.id = id;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}
