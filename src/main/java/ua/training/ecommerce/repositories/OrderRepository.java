package ua.training.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.training.ecommerce.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
