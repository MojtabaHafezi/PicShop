package at.shop.facade.commands.user;

import at.shop.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class CreateUserCommand {

    @NotNull
    @NonNull
    private String email;

    @NotNull
    @NonNull
    private String password;

    @NotNull
    @NonNull
    private String passwordConfirm;

    @NotNull
    @NonNull
    private Role role;
}
