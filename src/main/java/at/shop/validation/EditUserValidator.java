package at.shop.validation;

import at.shop.facade.commands.user.CreateUserCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class EditUserValidator  implements org.springframework.validation.Validator{

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CreateUserCommand.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateUserCommand command = (CreateUserCommand) o;
        validatePassword(errors, command);
    }

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
}
