package at.shop.domain;

import com.sun.istack.internal.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;




@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@Table(name = "products")
@Entity
public class Product extends BaseDomain {

    @NonNull
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "name")
    private String name;

    @NonNull
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "producer")
    private String producer;

    @NonNull
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "description")
    private String description;

    @NonNull
    @NotNull
    @Min(0)
    private Double price;


    @NonNull
    @NotNull
    @Min(0)
    private Integer stock;

    @NonNull
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "url")
    private String url;

    public Product setNewProduct(Product newProduct) {
        setName(newProduct.getName());
        setProducer(newProduct.getProducer());
        setDescription(newProduct.getDescription());
        setPrice(newProduct.getPrice());
        setStock(newProduct.getStock());
        setUrl(newProduct.getUrl());
        return this;
    }


}
