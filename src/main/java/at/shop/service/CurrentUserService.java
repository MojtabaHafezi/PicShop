package at.shop.service;

import at.shop.domain.CurrentUser;
import at.shop.domain.Role;
import at.shop.facade.UserFacade;
import at.shop.facade.views.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrentUserService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        UserView user = userFacade.getUser(email);
        if (user.getId() > 0) {
            return new CurrentUser(user);
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s was not found", email));
        }

    }

    //Does the current user have access to the user data?
    //admins in general and users can only change their own data
    public boolean hasAccessEditUser(CurrentUser user, Long id) {
        return user != null && (user.getRole() == Role.ROLE_ADMIN || user.getId().equals(id));
    }

    //Admin and employee
    public boolean hasAccessForProducts(CurrentUser user) {
        return user != null && (user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ROLE_EMPLOYEE);
    }

    public boolean hasAccessForViewProducts(CurrentUser user) {
        return user != null && (user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ROLE_EMPLOYEE || user
                .getRole() == Role.ROLE_USER);

    }

}

