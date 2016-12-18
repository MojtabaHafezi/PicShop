package at.shop.domain;
import at.shop.facade.views.UserView;
import org.springframework.security.core.authority.AuthorityUtils;


public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private UserView userView;

    public CurrentUser(UserView userView) {
        super(userView.getEmail(), userView.getPassword(), AuthorityUtils.createAuthorityList(userView.getRole().toString()));
        setUserView(userView);
    }


    public UserView getUser(){return  userView;}

    public String getEmail() {
        return userView.getEmail();
    }

    public Long getId() {
        return userView.getId();
    }

    public Role getRole() {
        return userView.getRole();
    }

    private void setUserView(UserView userView) {
            this.userView = userView;
    }

}
