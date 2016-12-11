package at.shop.persistence;

import at.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

}


/*

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> findByNameLike(String name);
    List<Product> findByProducerLike(String producer);
    List<Product> findByPriceLessThan(Double price);
    List<Product> findByStockGreaterThan(Integer stock);


}

 */