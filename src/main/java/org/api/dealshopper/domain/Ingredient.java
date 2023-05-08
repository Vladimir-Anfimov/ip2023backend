package org.api.dealshopper.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "name", nullable = false, unique = true)
    private String ingredientName;


    @ManyToMany(mappedBy = "ingredients")
    private List<Product> products;

    /*
    @ManyToMany
    @JoinTable(name = "ingr_prod",
              joinColumns = @JoinColumn(name = "ingredient_id"),
              inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

     */
}
/*
The nullable = false attribute ensures that the name value cannot be null,
 and unique = true attribute ensures that no two Ingredient entities have
 the same name value.
 */