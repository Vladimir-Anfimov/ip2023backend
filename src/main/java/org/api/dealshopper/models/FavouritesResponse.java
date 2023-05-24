package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.domain.Product;
import org.api.dealshopper.domain.Restaurant;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavouritesResponse {

    private List<Restaurant> favouriteRestaurants;
    private List<Product> favouriteProducts;
}
