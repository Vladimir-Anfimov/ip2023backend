package org.api.dealshopper.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "restaurants")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    private String address;

    private String phone;

    private String image;

    @OneToMany(mappedBy = "restaurant")
    private List<Product> menu;

    @OneToMany(mappedBy = "restaurant")
    private List<Schedule> schedule;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;

    @OneToMany(mappedBy = "restaurant")
    private List<DeliveryInfo> deliveryInfoList;

    @ManyToMany(mappedBy = "favouriteRestaurants")
    private List<User> restaurantFans;//favourite_restaurants, useri ce au ca restaurant favorit this

    public Restaurant(int id, String name, String address, String phone, String image)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }
}
