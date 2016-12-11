package at.shop.facade.views;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
@Builder
public class ProductView {
    private final Long id;
    //private final Integer version;
    private final String name;
    private final String producer;
    private final String description;
    private final Double price;
    private final Integer stock;
    private final String url;
}
