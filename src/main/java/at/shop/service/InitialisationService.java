package at.shop.service;

import at.shop.domain.Product;
import at.shop.domain.Role;
import at.shop.domain.User;
import at.shop.persistence.ProductRepository;
import at.shop.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InitialisationService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @PostConstruct
    protected void initDemoData(){
        log.info("------------ BEFORE INIT ------------");

        Product game1 = Product.of("Fallien","Hafezi","Platformer",0d,100,"default.png");
        Product game2 = Product.of("Knights to arms","Hafezi","Strategy",0d,100,"tomatoes.jpg");
        Product game3 = Product.of("Bowling", "Even", "Sports game 3d", 0d, 100, "bowl.jpg");
        Product game4 = Product.of("Basketball", "More", "Sports game 2d", 0d, 100, "basketball.png");


        String password = "123456";
        String password2 = "simple";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        User user1 = User.of("MHAFEZI@uclan",hashedPassword, Role.ROLE_ADMIN);
        hashedPassword = encoder.encode(password2);
        User user2 = User.of("Hello@world", hashedPassword, Role.ROLE_USER);




    /*  log.info(game1.toString());
        log.info(game2.toString());*/
        productRepository.save(Arrays.asList(game1,game2,game3,game4));
        userRepository.save(Arrays.asList(user1,user2));
        log.info(productRepository.findAll().toString());
        log.info(userRepository.findAll().toString());

        log.info("------------ AFTER INIT ------------");
    }
}
