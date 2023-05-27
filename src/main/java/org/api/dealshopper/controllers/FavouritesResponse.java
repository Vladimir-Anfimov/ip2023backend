package org.api.dealshopper.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.models.ProductDto;
import org.api.dealshopper.models.RestaurantDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouritesResponse {
    private List<ProductDto> products;
    private List<RestaurantDTO> restaurants;
}