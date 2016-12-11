package at.shop.presentation.shop;

import at.shop.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping(value="/shop")

public class ShopController {

    private final ProductFacade facade;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        return "index";
    }
    @RequestMapping(value = "/footer", method = RequestMethod.GET)
    public String footer(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        return "footer_new";
    }

}
