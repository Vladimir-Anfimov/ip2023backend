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
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    private List<Product> menu;

    private String address;

    @OneToMany
    private List<Schedule> schedule;

    @OneToMany
    private List<Review> reviews;

    private String phoneNumber;

    private String platform;

    private String location;


    public Restaurant(List<Product> menu, String address, List<Schedule> schedule, List<Review> reviews, String phoneNumber, String platform, String location) {
        this.menu = menu;
        this.address = address;
        this.schedule = schedule;
        this.reviews = reviews;
        this.phoneNumber = phoneNumber;
        this.platform = platform;
        this.location = location;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
