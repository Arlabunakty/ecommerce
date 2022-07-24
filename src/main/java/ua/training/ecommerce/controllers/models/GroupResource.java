package ua.training.ecommerce.controllers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import ua.training.ecommerce.models.GroupVariant;
import ua.training.ecommerce.models.ProductGroup;

public class GroupResource {
    @JsonProperty
    long id;
    @JsonProperty
    String groupName;
    @JsonProperty
    String price;
    @JsonProperty
    List<GroupVariant> variants;

    public GroupResource(ProductGroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.price = group.getPrice();
        this.variants = group.getGroupVariants();
    }
}
