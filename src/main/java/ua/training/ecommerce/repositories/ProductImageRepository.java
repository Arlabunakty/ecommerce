package ua.training.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.ecommerce.models.ProductImage;

@Repository("productImageRepository")
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}