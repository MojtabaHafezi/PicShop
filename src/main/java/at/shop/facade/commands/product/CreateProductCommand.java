package at.shop.facade.commands.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class CreateProductCommand {

    @NonNull
    @NotNull
    private String name;

    @NonNull
    @NotNull
    private String producer;

    @NonNull
    @NotNull
    private String description;

    @NonNull
    @NotNull
    private Double price;


    @NonNull
    @NotNull
    private Integer stock;

    @NonNull
    @NotNull
    private String url;

}