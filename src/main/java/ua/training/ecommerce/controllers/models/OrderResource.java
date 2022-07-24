package ua.training.ecommerce.controllers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import ua.training.ecommerce.models.Order;
import ua.training.ecommerce.models.OrderItem;

@Getter
public class OrderResource {
    @JsonProperty
    long id;
    @JsonProperty
    String name;
    @JsonProperty
    String address;
    @JsonProperty
    String city;
    @JsonProperty
    String zip;
    @JsonProperty
    String status;
    @JsonProperty
    String comment;
    @JsonProperty
    String totalPrice;
    @JsonProperty
    String type;
    @JsonProperty
    List<OrderItem> items;
    @JsonProperty
    LocalDateTime created;

    public OrderResource(Order o) {
        id = o.getId();
        name = o.getName();
        address = o.getAddress();
        city = o.getCity();
        zip = o.getZip();
        status = o.getStatus();
        comment = o.getComment();
        totalPrice = o.getTotalPrice();
        type = o.getType();
        created = o.getCreated();
        items = Collections.unmodifiableList(o.getItems());
    }
}
