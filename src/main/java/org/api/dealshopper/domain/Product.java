package org.api.dealshopper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float price;

    private float rating;

    private float deliveryCost;

    private float discount;

    @OneToMany
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    public Product(float price, float rating, float deliveryCost, float discount, List<Ingredient> ingredients, Restaurant restaurant) {
        this.price = price;
        this.rating = rating;
        this.deliveryCost = deliveryCost;
        this.discount = discount;
        this.ingredients = ingredients;
        this.restaurant = restaurant;
    }
}
