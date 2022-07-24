package ua.training.ecommerce.controllers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import ua.training.ecommerce.models.Product;

public class ProductResource {
    @JsonProperty
    long id;
    @JsonProperty
    String name;
    @JsonProperty
    String price;
    @JsonProperty
    String description;
    @JsonProperty
    Object group;

    public ProductResource(Product model) {
        id = model.getId();
        name = model.getName();
        price = model.getPrice();
        description = model.getDescription();
        group = model.getGroup();
    }

}
