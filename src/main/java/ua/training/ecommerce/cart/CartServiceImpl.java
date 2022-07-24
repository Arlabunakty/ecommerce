package ua.training.ecommerce.cart;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.ecommerce.cache.Cache;
import ua.training.ecommerce.models.GroupVariant;
import ua.training.ecommerce.models.Order;
import ua.training.ecommerce.models.OrderItem;
import ua.training.ecommerce.models.Product;
import ua.training.ecommerce.services.EcommerceService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final EcommerceService ecommerceService;
    private final Cache cache;

    @Override
    public String createNewCart() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void addProduct(String cartId, CartItem cartItem) {
        cache.addItemToList(cartId, cartItem, CartItem.class);
    }

    @Override
    public void removeProduct(String cartId, long productId) {
        CartItem itemToRemove = new CartItem();
        itemToRemove.setProductId(productId);

        cache.removeItemFromList(cartId, itemToRemove, CartItem.class);
    }

    @Override
    public void setProductQuantity(String cartId, long productId, int quantity) {
        cache.getList(cartId, CartItem.class).forEach(cartItem -> {
            try {
                if (cartItem.getProductId() == productId) {
                    cartItem.setQuantity(quantity);
                    cache.removeItemFromList(cartId, cartItem, CartItem.class);
                    cache.addItemToList(cartId, cartItem, CartItem.class);
                }
            } catch (Exception e) {
                log.error("setProductQuantity", e);
            }
        });
    }

    @Override
    public Set<CartItem> getItems(String cartId) {
        return new HashSet<>(cache.getList(cartId, CartItem.class));
    }

    @Override
    @Transactional
    public Order createOrder(String cartId, Order order) {
        if (order == null) {
            log.error("Order not set.");
            return null;
        }
        var cartItems = cache.getList(cartId, CartItem.class);

        addCartItemsToOrders(cartItems, order);
        var persistedOrder = ecommerceService.saveOrder(order);

        cache.removeItem(cartId);

        return persistedOrder;
    }

    private void addCartItemsToOrders(Collection<CartItem> cartItems, Order order) {
        cartItems.forEach(cartItem -> {
            Product prod = ecommerceService.getProduct(cartItem.getProductId());
            int qty = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;
            long variantId = cartItem.getVariantId();

            IntStream.range(0, qty)
                    .mapToObj(i -> new OrderItem())
                    .forEachOrdered(orderItem -> {
                        orderItem.setProduct(prod);
                        if (variantId > 0) {
                            GroupVariant v = new GroupVariant();
                            v.setId(variantId);
                            orderItem.setGroupVariant(v);
                        }
                        orderItem.setOrder(order);
                        order.getItems().add(orderItem);
                    });
        });
    }
}
