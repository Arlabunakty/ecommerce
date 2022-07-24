package ua.training.ecommerce.controllers;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.training.ecommerce.controllers.models.GroupResource;
import ua.training.ecommerce.models.ProductGroup;
import ua.training.ecommerce.services.EcommerceService;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final EcommerceService ecommerceService;

    @GetMapping
    public List<GroupResource> index() {
        return ecommerceService.getGroups().stream()
                .map(this::mapToResource)
                .toList();
    }

    private GroupResource mapToResource(ProductGroup g) {
        return new GroupResource(g);
    }

    @GetMapping("/{id}")
    public GroupResource view(@PathVariable("id") long id) {
        return mapToResource(ecommerceService.getGroup(id));
    }

    @PostMapping(value = "/{id}")
    public GroupResource edit(@PathVariable(value = "id", required = false) long id, @RequestBody @Valid ProductGroup group) {
        ProductGroup updatedGroup = ecommerceService.getGroup(id);

        updatedGroup.setGroupName(group.getGroupName());
        updatedGroup.setPrice(group.getPrice());
        updatedGroup.setGroupVariants(group.getGroupVariants());

        return mapToResource(ecommerceService.saveGroup(updatedGroup));
    }

    @PostMapping
    public GroupResource create(@RequestBody @Valid ProductGroup group) {
        return mapToResource(ecommerceService.saveGroup(group));
    }
}