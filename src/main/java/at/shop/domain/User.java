package at.shop.domain;

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Size(min = 6, max = 150)
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @NonNull
    @NotNull
    @Size(min = 6, max = 150)
    @Column(name = "password", unique = true)
    private String password;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User editUser(User newUser){
        this.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        this.setRole(newUser.getRole());
        this.setEmail(newUser.getEmail());
        return this;
    }
}