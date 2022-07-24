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
import ua.training.ecommerce.controllers.models.OrderResource;
import ua.training.ecommerce.models.Order;
import ua.training.ecommerce.services.EcommerceService;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final EcommerceService ecommerceService;

    @GetMapping
    public List<OrderResource> index() {
        return ecommerceService.getOrders()
                .stream()
                .map(this::mapToOrderResource)
                .toList();
    }

    @PostMapping
    public OrderResource create(@RequestBody @Valid Order order) {
        return mapToOrderResource(ecommerceService.saveOrder(order));
    }

    @GetMapping("/{id}")
    public OrderResource view(@PathVariable("id") long id) {
        Order order = ecommerceService.getOrder(id);
        return mapToOrderResource(order);
    }

    @PostMapping(value = "/{id}")
    public OrderResource edit(@PathVariable("id") long id, @RequestBody @Valid Order updatedOrder) {
        updatedOrder.setId(id);
        return mapToOrderResource(ecommerceService.updateOrder(updatedOrder));
    }

    private OrderResource mapToOrderResource(Order order) {
        return new OrderResource(order);
    }
}
