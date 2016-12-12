package at.shop.presentation.shop;

import at.shop.domain.Role;
import at.shop.facade.UserFacade;
import at.shop.facade.commands.user.CreateUserCommand;
import at.shop.facade.views.UserView;
import at.shop.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@ControllerAdvice
@RequestMapping(value = "/")
public class ShopController {

    private final UserFacade userFacade;
    private final UserValidator userValidator;

    @InitBinder("command")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    //with controlleradvice and this method every view can have access to the current user via "currentUser"
    @ModelAttribute("currentUser")
    public UserDetails getCurrentUser(Authentication authentication) {
        return (authentication == null) ? null : (UserDetails) authentication.getPrincipal();
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
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        return new ModelAndView("users/showAll", "users", userFacade.getUsers());
    }

    @PreAuthorize("@currentUserService.hasAccess(principal, #id)")
    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        return new ModelAndView("users/edit", "user", userFacade.getUser(id));
    }

    //REGISTRATION
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage(ModelAndView mv) {
        mv.setViewName("users/register");
        mv.getModelMap().addAttribute("user", CreateUserCommand.of("xyz@abc.de","","", Role.USER));
        return mv;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView handleUserCreateForm(
            @Valid @ModelAttribute("user") CreateUserCommand command, BindingResult bindingResult, ModelAndView mv) {
        if (bindingResult.hasErrors()) {
            mv.setViewName("users/register");
            return mv;
        }
        try {
            command.setRole(Role.USER);
            UserView userView = userFacade.createUser(command);
            if(userView.getId() > 0 ) {
                mv.setViewName("redirect:/shop");
            } else  {
                mv.setViewName("users/register");
                return mv;
            }
        } catch (DataIntegrityViolationException e) {
            mv.setViewName("users/register");
            return mv;
        }
        mv.setViewName("users/register");
        return mv;
    }

    //LOGIN
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("users/login", "error", error);
    }

    //LOGOUT
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }


}
