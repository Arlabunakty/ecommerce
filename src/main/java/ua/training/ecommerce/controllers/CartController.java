package ua.training.ecommerce.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.training.ecommerce.cart.CartItem;
import ua.training.ecommerce.cart.CartService;
import ua.training.ecommerce.controllers.models.OrderResource;
import ua.training.ecommerce.models.Order;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/")
    public Map<String, String> create() {
        return Collections.singletonMap("id", cartService.createNewCart());
    }

    @PostMapping("/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@PathVariable("cartId") String cartId, @RequestBody CartItem cartItem) {
        cartService.addProduct(cartId, cartItem);
    }

    @GetMapping("/{cartId}")
    public Set<CartItem> getCartItems(@PathVariable("cartId") String cartId) {
        return cartService.getItems(cartId);
    }

    @DeleteMapping("{cartId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable("cartId") String cartId, @PathVariable("productId") long productId) {
        cartService.removeProduct(cartId, productId);
    }

    @PostMapping("{cartId}/quantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setProductQuantity(@PathVariable("cartId") String cartId, @RequestBody CartItem cartItem) {
        cartService.setProductQuantity(cartId, cartItem.getProductId(), cartItem.getQuantity());
    }

    @PostMapping("{cartId}/order")
    public OrderResource createOrder(@PathVariable("cartId") String cartId, @RequestBody @Valid Order order) {
        if (order == null) {
            log.error("Order not in POST");
            return null;
        }
        return new OrderResource(cartService.createOrder(cartId, order));
    }

}
