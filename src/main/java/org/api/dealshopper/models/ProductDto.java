package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.domain.Ingredient;
import org.api.dealshopper.domain.Product;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto
{
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private List<String> ingredients;

    public ProductDto(Product product)
    {
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        imageUrl = product.getImage();

        ingredients = product.getIngredients().stream().map(Ingredient::getIngredientName).toList();
    }
}
