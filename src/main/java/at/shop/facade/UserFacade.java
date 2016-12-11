package at.shop.facade;


import at.shop.domain.Role;
import at.shop.domain.User;
import at.shop.facade.commands.user.CreateUserCommand;
import at.shop.facade.commands.user.DeleteUserCommand;
import at.shop.facade.views.UserView;
import at.shop.persistence.UserRepository;
import at.shop.service.UserService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserFacade {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserView createUser(@NotNull @Valid CreateUserCommand command) {
        try {
            User user = User.of("", "", Role.USER);
            user.setEmail(command.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(command.getPassword()));
            user.setRole(command.getRole());
            Optional<User> createdUser = userService.createUser(user);
            return createUserView(createdUser);

        } catch (Exception ex) {
            log.warn("Encountered a problem while creating the user.", ex);
            return errorView();
        }
    }


    public UserView editUser(@NotNull @Valid Long id, @NotNull @Valid CreateUserCommand command) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            Optional<User> optional = Optional.of(User.of(command.getEmail(), command.getPassword(), command
                    .getRole()));
            Optional<User> updatedUser = userService.editUser(existingUser, optional);
            return createUserView(updatedUser);
        } catch (Exception ex) {
            log.warn("Encountered a problem while creating the user.", ex);
            return errorView();
        }
    }

    public void deleteUser(@NotNull @Valid DeleteUserCommand command) {
        userService.deleteUser(command.getId());
    }

    public List<UserView> getUsers() {
        return _mapUsers(userRepository.findAll());
    }

    public UserView getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return (user.isPresent() ? createUserView(user) : errorView());
    }

    public UserView getUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return (user.isPresent() ? createUserView(user) : errorView());
    }

    private List<UserView> _mapUsers(List<User> users) {
        return users.stream()
                .map(user -> UserView.builder().email(user.getEmail()).password(user.getPassword()).role(user.getRole())
                        .build()).collect(
                        Collectors.toList());
    }

    //@Returns a valid view from the given user
    private UserView createUserView(Optional<User> user) {
        boolean valid = user.isPresent();
        return UserView.builder()
                .id(valid ? user.get().getId() : -1L)
                .email(valid ? user.get().getEmail() : "unknown")
                .password(valid ? user.get().getPassword() : "unknown")
                .role(valid ? user.get().getRole() : Role.USER)
                .build();
    }

    //@Returns an invalid view
    private UserView errorView() {
        return UserView.of(-1L, "unknown", "unknown", Role.USER);
    }
}
