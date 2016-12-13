package at.shop.service;

import at.shop.domain.CurrentUser;
import at.shop.domain.Role;
import at.shop.domain.User;
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
        if(user.getId()>0) {
            return new CurrentUser(User.of(user.getEmail(), user.getPassword(), user.getRole()));
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s was not found",email));
        }

    }

    //Does the current user have access to the user data?
    //admins in general and users can only change their own data
    public boolean hasAccess(CurrentUser user, Long id) {
        return user != null && (user.getRole()== Role.ROLE_ADMIN || user.getId().equals(id));
    }

}

