package at.shop.facade.commands.user;

import at.shop.domain.Role;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
