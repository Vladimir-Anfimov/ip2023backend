package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.domain.Restaurant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Integer id;
    private String name;

    private String address;

    private String phone;

    private String image;
    private double rating;
    private String platform;

    public RestaurantDTO(Restaurant restaurant, String platform) {
        this.phone = restaurant.getPhone();
        this.name = restaurant.getName();
        this.image = restaurant.getImage();
        this.rating = restaurant.getRating();
        this.address = restaurant.getAddress();
        this.platform = platform;
        this.id = restaurant.getId();
    }
}
