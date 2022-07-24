package ua.training.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.ecommerce.models.ProductGroup;

@Repository("groupRepository")
public interface GroupRepository extends JpaRepository<ProductGroup, Long> {
}