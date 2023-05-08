package org.api.dealshopper.domain;

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

    public Restaurant(int id, String name, String address, String phone, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Product> getMenu() {
        return menu;
    }

    public void setMenu(List<Product> menu) {
        this.menu = menu;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
