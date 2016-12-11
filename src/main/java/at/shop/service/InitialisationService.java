package at.shop.service;

import at.shop.domain.Product;
import at.shop.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostConstruct
    protected void initDemoData(){
        log.info("------------ BEFORE INIT ------------");

        Product game1 = Product.of("Fallien","Hafezi","Platformer",10d,100,"default.png");
        Product game2 = Product.of("Knights to arms","Hafezi","Strategy",12d,100,"tomatoes.jpg");
        Product game3 = Product.of("Bowling", "Ubi", "Sports game 3d", 13.23d, 100, "bowl.jpg");
        Product game4 = Product.of("Basketball", "Mor", "Sports game 2d", 11.15d, 100, "basketball.png");



    /*    log.info(game1.toString());
        log.info(game2.toString());*/
        productRepository.save(Arrays.asList(game1,game2,game3,game4));
        log.info(productRepository.findAll().toString());

        log.info("------------ AFTER INIT ------------");
    }
}