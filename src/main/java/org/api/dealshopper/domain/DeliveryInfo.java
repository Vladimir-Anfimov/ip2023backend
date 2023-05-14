package org.api.dealshopper.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "delivery_info")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class DeliveryInfo {

    @EmbeddedId
    private DeliveryInfoId id;

    @Column(name = "delivery_cost")
    private Integer deliveryCost;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @ManyToOne
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;

    public DeliveryInfo() {}

    public DeliveryInfo(DeliveryInfoId id, Integer deliveryCost) {
        this.id = id;
        this.deliveryCost=deliveryCost;
    }

    public DeliveryInfoId getId() {
        return id;
    }

    public void setId(DeliveryInfoId id) {
        this.id = id;
    }

    public Integer getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double getEfficiency()
    {
        return ((double) getDeliveryTime() + (double) getDeliveryCost()) / 2;
    }

}
