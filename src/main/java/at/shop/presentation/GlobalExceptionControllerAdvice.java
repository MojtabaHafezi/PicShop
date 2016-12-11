package at.shop.presentation;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleCustomException(RuntimeException ex) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("errCode", ex.getCause());
        model.addObject("errMsg", ex.getMessage());

        return model;

    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleAllException(Throwable ex) {

        ModelAndView model = new ModelAndView("error/generic_error");
        model.addObject("errMsg", "this is Exception.class");

        return model;

    }

}