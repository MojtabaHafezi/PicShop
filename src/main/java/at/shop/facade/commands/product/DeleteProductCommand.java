package at.shop.facade.commands.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class DeleteProductCommand {

    @NotNull
    @NonNull
    private Long id;

}
