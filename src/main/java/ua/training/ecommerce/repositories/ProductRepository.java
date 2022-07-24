package ua.training.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.ecommerce.models.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

}