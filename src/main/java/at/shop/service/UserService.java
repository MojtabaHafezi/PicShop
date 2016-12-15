package at.shop.service;

import at.shop.domain.User;
import at.shop.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<User> createUser(@NotNull @Valid User user) {
        return Optional.of(userRepository.save(user));
    }

    @Transactional
    public Optional<User> editUser(Optional<User> existingUser, Optional<User> newUser) {

        if (existingUser.isPresent()) {

            existingUser.get().editUser(newUser.get());
            return Optional.of(userRepository.save(existingUser.get()));

        }
        return Optional.empty();
    }

    @Transactional
    public void deleteUser(@NotNull @Valid Long id) {

        userRepository.delete(id);
    }


}
