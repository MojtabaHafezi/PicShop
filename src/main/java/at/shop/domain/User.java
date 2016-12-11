package at.shop.domain;

import com.sun.istack.internal.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@Table(name = "user")
@Entity
public class User extends BaseDomain {


    @NonNull
    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "email", unique = true)
    private String email;

    @NonNull
    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "password", unique = true)
    private String password;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User editUser(User newUser){
        this.setPassword(newUser.getPassword());
        this.setRole(newUser.getRole());
        this.setEmail(newUser.getEmail());
        return this;
    }
}