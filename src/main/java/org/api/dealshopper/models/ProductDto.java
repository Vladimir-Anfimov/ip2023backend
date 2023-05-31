package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.dealshopper.domain.Product;

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
    private String ingredients;

    private boolean favourite;

    public ProductDto(Product product)
    {
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        imageUrl = product.getImage();
        ingredients = product.getIngredients();
        favourite = false;
    }

    public ProductDto(Product product, Integer userId)
    {
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        imageUrl = product.getImage();
        ingredients = product.getIngredients();
        favourite = product.getProductFans().stream().anyMatch(user -> user.getId() == userId);
    }
}

