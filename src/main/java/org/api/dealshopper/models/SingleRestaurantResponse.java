package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleRestaurantResponse
{
    private String name;
    private String platformName;
    private List<Category> categories;
}
