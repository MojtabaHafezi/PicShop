package at.shop.validation;

import at.shop.facade.ProductFacade;
import at.shop.facade.commands.product.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ProductValidator implements Validator {

    private final ProductFacade productFacade;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CreateProductCommand.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}

/*
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

    public boolean supports(Class<?> aClass) {
        return aClass.equals(CreateUserCommand.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateUserCommand command = (CreateUserCommand) o;
        validateEmail(errors, command);
        validatePassword(errors, command);
    }

    //password validation: passwords match and wrong input
    private void validatePassword(Errors errors, CreateUserCommand command) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (!command.getPassword().equals(command.getPasswordConfirm())) {
            errors.rejectValue("password", "password.match_error", "Passwords do not match!");
        } else {
            if (command.getPassword().length() < 6 || command.getPassword().length() >= 150) {
                errors.rejectValue("password", "password.length_error", "Password must be between 6 and 50 characters");
            }
        }

    }
    //email validation: existing or wrong input
    private void validateEmail(Errors errors, CreateUserCommand command) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        //if a userview is returned then the email is already in use
        if (userFacade.getUser(command.getEmail()).getId() > 0) {
            errors.rejectValue("email", "email.exist_error", "This mail address is already in use.");
        } else {
            if (command.getEmail().length() < 6 || command.getEmail().length() >= 150) {
                errors.rejectValue("email", "email.length_error", "The mail address must be between 6 and 50 characters");
            }
        }


    }
}
*/