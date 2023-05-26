package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteRestaurantRequest {
    private String action;
    private Integer restaurantId;
}