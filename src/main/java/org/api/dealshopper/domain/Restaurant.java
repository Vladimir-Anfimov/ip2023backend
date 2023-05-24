package org.api.dealshopper.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
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
    private double rating;
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

    public Restaurant(int id, String name, String address, String phone, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public Restaurant(int id, String name, String address, String phone, String image, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeliveryInfoList(List<DeliveryInfo> deliveryInfoList) {
        this.deliveryInfoList = deliveryInfoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }


    public List<Product> getMenu() {
        return menu;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<DeliveryInfo> getDeliveryInfoList() {
        return deliveryInfoList;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
