package at.shop.facade;

import at.shop.domain.Product;
import at.shop.facade.commands.product.CreateProductCommand;
import at.shop.facade.commands.product.DeleteProductCommand;
import at.shop.facade.commands.product.EditProductCommand;
import at.shop.facade.views.ProductView;
import at.shop.persistence.ProductRepository;
import at.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductFacade {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductView createProduct(@NotNull @Valid CreateProductCommand command){
        try{
            Optional<Product> createdProduct = productService.createProduct(Product.of(command.getName(),
                    command.getProducer(),command.getDescription(),command.getPrice(),command.getStock(),command.getUrl()));
            return createProductView(createdProduct,command.getName());
        } catch (Exception ex) {
            log.warn("Encountered a problem while creating the product.", ex);
            return errorView();
        }
    }

    public ProductView editProduct(@NotNull @Valid Long id, @NotNull @Valid EditProductCommand command) {
        try{
            Optional<Product> existingProduct = productRepository.findById(id);
            Optional<Product> optional = Optional.of(Product.of(command.getName(),command.getProducer()
                    ,command.getDescription(),command.getPrice(),command.getStock(),command.getUrl()));

            Optional<Product> updatedProduct = productService.editProduct(existingProduct, optional);
            return createProductView(updatedProduct,command.getName());
        } catch (Exception ex) {
            log.warn("Encountered a problem while editing the product.", ex);
            return errorView();
        }
    }

    public void deleteProduct(@NotNull @Valid DeleteProductCommand command) {
        productService.deleteProduct(command.getId());
     //  productRepository.delete(command.getId());
    }


    public ProductView getProduct(String name){
        Optional<Product> product = productRepository.findByName(name);
        return createProductView(product,"");
    }

    public List<ProductView> getProducts(){
        return _mapProducts(productRepository.findAll());
    }

    public ProductView getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return (product.isPresent() ? createProductView(product,"") : errorView());
    }

    public List<ProductView> searchProducts(Optional<String> search) {
        return _mapProducts(productRepository.findByNameLike(String.format("%%%s%%",search.orElse(""))));
    }

    public List<ProductView> getProductsByProducer(String producer) {
        return _mapProducts(productRepository.findByProducerLike(producer));
    }

    public List<ProductView> getProductsByPrice(Double price) {
        return _mapProducts(productRepository.findByPriceLessThan(price));
    }

    public List<ProductView> getProductsByStock(Integer stock) {
        return _mapProducts(productRepository.findByStockGreaterThan(stock));
    }

    //@Returns an invalid view
    private ProductView errorView() {
        return ProductView.of(-1l,"unknown","unknown","unknown",-1d,-1,"default.png");
    }

    //@Returns a valid view from the given Product
    private ProductView createProductView(Optional<Product> product, String defaultName){
        boolean valid = product.isPresent();
        return ProductView.builder()
                .id(valid ? product.get().getId() : -1L)
                .name(valid ? product.get().getName() : defaultName)
                .description(valid ? product.get().getDescription() : "unknown")
                .producer(valid ? product.get().getProducer() : "unknown")
                .price(valid ? product.get().getPrice() : -1d)
                .stock(valid ? product.get().getStock() : -1)
                .url(valid ? product.get().getUrl() : "default.png")
                .build();
    }

    private List<ProductView> _mapProducts(List<Product> products){
        return products.stream().map(product -> ProductView.builder()
                .name(product.getName())
                .id(product.getId())
                .producer(product.getProducer())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .url(product.getUrl()).build()).collect(Collectors.toList());
    }

}
