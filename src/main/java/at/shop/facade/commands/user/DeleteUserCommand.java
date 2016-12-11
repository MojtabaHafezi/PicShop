package at.shop.facade.commands.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class DeleteUserCommand {
    @NotNull
    @NonNull
    private Long id;
}
