package at.shop.presentation.shop;

import at.shop.facade.UserFacade;
import at.shop.facade.commands.user.CreateUserCommand;
import at.shop.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping(value="/")
public class ShopController {

    private final UserFacade userFacade;
    private final UserValidator userValidator;

    @InitBinder("command")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }


    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String index(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        return "index";
    }

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        return new ModelAndView("users", "users", userFacade.getUsers());
    }


    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        return new ModelAndView("user", "user", userFacade.getUser(id));
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        return new ModelAndView("user_create", "command", new CreateUserCommand());
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("command") CreateUserCommand command, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_create";
        }
        try {
            userFacade.createUser(command);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("users/login", "error", error);
    }



}
