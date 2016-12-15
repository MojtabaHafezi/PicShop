package at.shop.service;

import at.shop.domain.Product;
import at.shop.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Optional<Product> createProduct(@NotNull @Valid Product product) {
        return Optional.of(productRepository.save(product));
    }

    @Transactional
    public Optional<Product> editProduct(Optional<Product> existingProduct, Optional<Product> newProduct){
        if(existingProduct.isPresent()){
            existingProduct.get().setNewProduct(newProduct.get());
            return Optional.of(productRepository.save(existingProduct.get()));
        }
        return Optional.empty();
    }
    @Transactional
    public void deleteProduct(@NotNull @Valid Long id){
        productRepository.delete(id);
    }

}
