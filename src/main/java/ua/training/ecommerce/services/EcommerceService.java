package ua.training.ecommerce.services;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.ecommerce.models.Order;
import ua.training.ecommerce.models.Product;
import ua.training.ecommerce.models.ProductGroup;
import ua.training.ecommerce.repositories.GroupRepository;
import ua.training.ecommerce.repositories.OrderRepository;
import ua.training.ecommerce.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class EcommerceService {
    private final ProductRepository productRepository;
    private final GroupRepository groupRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProduct(long id) {
        return productRepository.getReferenceById(id);
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductGroup> getGroups() {
        return groupRepository.findAll();
    }

    @NonNull
    @Transactional(readOnly = true)
    public ProductGroup getGroup(long id) {
        return groupRepository.getReferenceById(id);
    }

    @Transactional
    public ProductGroup saveGroup(ProductGroup group) {
        if (group.getGroupVariants() != null) {
            group.getGroupVariants().forEach(gv -> gv.setGroup(group));
        }
        return groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrder(long id) {
        return orderRepository.getReferenceById(id);
    }

    @Transactional
    public Order saveOrder(Order order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        if (getOrder(order.getId()) != null) {
            return orderRepository.save(order);
        } else {
            return null;
        }
    }
}
