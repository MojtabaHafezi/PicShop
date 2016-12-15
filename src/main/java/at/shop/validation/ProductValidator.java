package at.shop.validation;

import at.shop.facade.ProductFacade;
import at.shop.facade.commands.product.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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
    CreateProductCommand command = (CreateProductCommand) o;
    validateProduct(errors,command);
    }

    private void validateProduct(Errors errors, CreateProductCommand command) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","NotEmpty", "Please enter a name.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"description","NotEmpty", "Please enter a description.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"producer","NotEmpty", "Please enter a producer.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"price","NotEmpty", "Please enter a price.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"stock","NotEmpty", "Please enter a stock.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"url","NotEmpty", "Please enter a url.");

        if(command.getName().length() < 5 || command.getName().length() > 150) {
            errors.rejectValue("name","name.length_error","Length must be between 5 and 150 characters");
        }
        if(command.getDescription().length() < 5 || command.getDescription().length() > 150) {
            errors.rejectValue("description","description.length_error","Description must be between 5 and 150 characters");
        }
        if(command.getProducer().length() < 5 || command.getProducer().length() > 150) {
            errors.rejectValue("producer","producer.length_error","producer must be between 5 and 150 characters");
        }
        if(command.getUrl().length() < 5 || command.getUrl().length() > 150) {
            errors.rejectValue("url","url.length_error","url must be between 5 and 150 characters");
        }
        if(command.getPrice() < 0 || command.getPrice() > Double.MAX_VALUE) {
            errors.rejectValue("price","price.size_error","Price must be neither negative nor too high");
        }
        if(command.getStock() < 0 || command.getStock() > Integer.MAX_VALUE){
            errors.rejectValue("stock","stock.size_error","stock cant be negative or too big");

        }


    }
}