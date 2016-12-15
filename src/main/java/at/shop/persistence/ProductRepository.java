package at.shop.persistence;


import at.shop.domain.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Cacheable("productById")
    Optional<Product> findById(Long id);
    @Cacheable("productByName")
    Optional<Product> findByName(String name);
    @Cacheable("productsByName")
    List<Product> findByNameLike(String name);
    List<Product> findByProducerLike(String producer);
    List<Product> findByPriceLessThan(Double price);
    List<Product> findByStockGreaterThan(Integer stock);


}
