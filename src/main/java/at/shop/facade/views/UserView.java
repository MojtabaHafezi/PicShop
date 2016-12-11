package at.shop.facade.views;

import at.shop.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
@Builder
public class UserView {
    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
}
