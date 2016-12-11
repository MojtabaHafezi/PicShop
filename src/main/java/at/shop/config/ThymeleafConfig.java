package at.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;

@Configuration
public class ThymeleafConfig {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private  SpringTemplateEngine templateEngine;

    @PostConstruct
    public void configureThymeleaf() {
        templateEngine.setMessageSource(messageSource);
    }
}