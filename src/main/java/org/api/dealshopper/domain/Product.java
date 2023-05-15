package org.api.dealshopper.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "discount")
    private Double discount = 0.0;

    @Column(name = "delivery_platform", nullable = false)
    private String deliveryPlatform;

    @Column(name = "category")
    private String category;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    private String ingredients;

    /*@ManyToMany
    @JoinTable(name = "ingr_prod",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName ="id" ),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id",referencedColumnName ="id"))
    private List<Ingredient> ingredients;*/


    @ManyToMany(mappedBy = "favouriteProducts")
    private List<User> productFans;//favourite_products, useri ce au ca produs favorit this
}
