package ua.training.ecommerce.cart;

import java.util.Set;
import ua.training.ecommerce.models.Order;

public interface CartService {
    String createNewCart();

    void addProduct(String cartId, CartItem cartItem);

    void removeProduct(String cartId, long productId);

    void setProductQuantity(String cartId, long productId, int quantity);

    Set<CartItem> getItems(String cartId);

    Order createOrder(String cartId, Order order);
}
