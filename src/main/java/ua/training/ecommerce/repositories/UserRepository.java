package ua.training.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ua.training.ecommerce.models.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    @Nullable
    User findByEmail(String email);
}

