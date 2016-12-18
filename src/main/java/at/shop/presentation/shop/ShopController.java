package at.shop.presentation.shop;

import at.shop.domain.CurrentUser;
import at.shop.domain.Role;
import at.shop.facade.UserFacade;
import at.shop.facade.commands.user.CreateUserCommand;
import at.shop.facade.commands.user.DeleteUserCommand;
import at.shop.facade.views.UserView;
import at.shop.validation.EditUserValidator;
import at.shop.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@ControllerAdvice
@RequestMapping(value = "/")
public class ShopController {

    private final UserFacade userFacade;
    private final UserValidator userValidator;
    private final EditUserValidator editUserValidator;

    //validation happens on the modelattribute "user"
    @InitBinder("user")
    public void initUserBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @InitBinder("userEdit")
    public void initEditUserBinder(WebDataBinder binder) {binder.addValidators(editUserValidator);}



    //with controlleradvice and this method every view can have access to the current user via "currentUser"
    @ModelAttribute("currentUser")
    public CurrentUser getCurrentUser(Authentication authentication) {
        if (authentication == null)
            return null;
        CurrentUser temp = (CurrentUser) authentication.getPrincipal();
        CurrentUser currentUser = new CurrentUser(userFacade.getUser(temp.getEmail()));
        return currentUser;
    }

    //HOME
    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String index(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    //Only the admin has access to this page
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        List<UserView> userViews = userFacade.getUsers();
        model.addAttribute("users", userViews);
        return "users/showAll";
    }

    //EDIT USER DATA
    //only admin and user himself can edit the data - principal from security core
    @PreAuthorize("@currentUserService.hasAccessEditUser(principal, #id)")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ModelAndView editUserPage(@PathVariable Long id, ModelAndView mv) {
        UserView userView = userFacade.getUser(id);
        mv.setViewName("users/edit");
        mv.getModelMap().addAttribute("userEdit", CreateUserCommand
                .of(userView.getEmail(), "", "", userView.getRole()));
        return mv;
    }

    @PreAuthorize("@currentUserService.hasAccessEditUser(principal, #id)")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public ModelAndView handleEditUserPage(
            @PathVariable Long id,
            @Valid @ModelAttribute("userEdit") CreateUserCommand command, BindingResult bindingResult, ModelAndView mv) {
        if(bindingResult.hasErrors()){
            mv.setViewName("users/edit");
            return mv;
        }
        UserView userView = userFacade.editUser(id,command);
        if (userView.getId() > 0) {
            mv.setViewName("redirect:/shop");
        } else {
            mv.setViewName("users/edit");
        }
        return mv;
    }


    //REGISTRATION
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage(ModelAndView mv) {
        mv.setViewName("users/register");

        CreateUserCommand command = CreateUserCommand.of("", "", "", Role.ROLE_USER);
        mv.getModelMap().addAttribute("user", command);
        return mv;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView handleRegisterPage(
            @Valid @ModelAttribute("user") CreateUserCommand command,BindingResult bindingResult, ModelAndView mv) {
        if (bindingResult.hasErrors()) {
            mv.setViewName("users/register");
            return mv;
        }
        command.setRole(Role.ROLE_USER);
        UserView userView = userFacade.createUser(command);
        if (userView.getId() > 0) {
            mv.setViewName("redirect:/shop");
        } else {
            mv.setViewName("users/register");
        }
        return mv;
    }

    //LOGIN
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("users/login", "error", error);
    }

    //LOGOUT
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            auth = null;
        }
        return "redirect:/login";
    }

    //CREATE USER
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "user/add", method = RequestMethod.GET)
    public ModelAndView addUserWithForm(ModelAndView mv) {
        mv.setViewName("users/create");
        mv.getModelMap().addAttribute("user", CreateUserCommand.of("", "", "", Role.ROLE_USER));
        return mv;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    public ModelAndView handleAddUserForm(
            @Valid @ModelAttribute("user") CreateUserCommand command, BindingResult bindingResult, ModelAndView mv) {
        if (bindingResult.hasErrors()) {
            mv.setViewName("users/create");
            return mv;
        }
        UserView userView = userFacade.createUser(command);
        if (userView.getId() > 0) {
            mv.setViewName("redirect:/shop");
        } else {
            mv.setViewName("users/create");
        }
        return mv;
    }

    //DELETE USERS
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "user/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable Long id, ModelAndView mv) {
        userFacade.deleteUser(DeleteUserCommand.of(id));
        mv.setViewName("redirect:/users/");
        return mv;
    }
}
