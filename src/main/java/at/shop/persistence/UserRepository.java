package at.shop.persistence;

import at.shop.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Cacheable("user")
    Optional<User> findById(Long id);
    @Cacheable("users")
    Optional<User> findByEmail(String email);

}