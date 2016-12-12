package at.shop.presentation;

import at.shop.facade.ProductFacade;
import at.shop.facade.commands.product.CreateProductCommand;
import at.shop.facade.commands.product.DeleteProductCommand;
import at.shop.facade.commands.product.EditProductCommand;
import at.shop.facade.views.ProductView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@ControllerAdvice
@RequestMapping(value="/product")
public class ProductController {

    //with controlleradvice and this method every view can have access to the current user via "currentUser"
    @ModelAttribute("currentUser")
    public UserDetails getCurrentUser(Authentication authentication) {
        return (authentication == null) ? null : (UserDetails) authentication.getPrincipal();
    }

    private final ProductFacade facade;

    @RequestMapping(value="/all", method = RequestMethod.GET)
    public String showAllProducts(Model model) {

        List<ProductView> productViews = facade.getProducts();
        model.addAttribute("products", productViews);
        return "product/showAllProducts";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView showProductWithPath(@PathVariable Long id, ModelAndView mv, BindingResult bindingResult){
        return _showProduct(id,mv,bindingResult);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView addProductWithForm(ModelAndView mv) {
        mv.setViewName("product/createForm");
        mv.getModelMap().addAttribute("product", CreateProductCommand.of("Enter name",
                "Enter producer", "Description",10d,10, "default.png"));
        return mv;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ModelAndView handleAddProductWithForm(@Valid @ModelAttribute("product") CreateProductCommand command,
                                                 BindingResult bindingResult, ModelAndView mv){
        if(bindingResult.hasErrors()){
            mv.setViewName("product/createForm");
            return mv;
        }
        ProductView productView = facade.createProduct(command);
        if(productView.getId()> 0) {
            mv.setViewName("redirect:/product/all");
        } else {
            if(StringUtils.isEmptyOrWhitespace(command.getName()) || command.getName()==null){
                bindingResult.rejectValue("name", "error", "Name is not valid");
            }
            if(StringUtils.isEmptyOrWhitespace(command.getDescription()) || command.getDescription()==null){
                bindingResult.rejectValue("description", "error", "Description is not valid");
            }
            if(StringUtils.isEmptyOrWhitespace(command.getProducer()) || command.getProducer()==null){
                bindingResult.rejectValue("producer", "error", "Producer is not valid");
            }
            if(command.getPrice() == null || command.getPrice() < 0 ){
                bindingResult.rejectValue("price", "error", "Price is not valid");
            }
            if(command.getStock() == null || command.getStock() < 0){
                bindingResult.rejectValue("stock", "error", "Stock is not valid");
            }
            if(StringUtils.isEmptyOrWhitespace(command.getUrl()) || command.getUrl()==null){
                bindingResult.rejectValue("url", "error", "URL is not valid");
            }

            mv.setViewName("product/createForm");
        }
        return mv;
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detailProduct(@PathVariable Long id, ModelAndView mv) {
        ProductView productView = facade.getProduct(id);
        mv.setViewName("product/detail");
        mv.getModelMap().addAttribute("product",productView);
        return mv;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public ModelAndView showEditProduct(@PathVariable Long id, ModelAndView mv) {
        ProductView productView = facade.getProduct(id);
        mv.setViewName("product/editForm");
        mv.getModelMap().addAttribute("product", EditProductCommand.of(productView.getName()
                ,productView.getProducer(),productView.getDescription(),productView.getPrice(),productView.getStock(),
                productView.getUrl()));
        return mv;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public ModelAndView handleEditProduct(@PathVariable Long id, @Valid @ModelAttribute("product") EditProductCommand command,
                                          BindingResult bindingResult, ModelAndView mv){
        ProductView productView = facade.editProduct(id, command);
        if(productView.getId() > 0) {
            mv.setViewName("redirect:/product/all");
        } else {
            mv.setViewName("product/editForm");
        }
        return mv;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteProduct(@PathVariable Long id, ModelAndView mv) {
        facade.deleteProduct(DeleteProductCommand.of(id));
        mv.setViewName("redirect:/product/all");
        return mv;
    }

    private ModelAndView _showProduct(@NotNull Long id, ModelAndView mv, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            mv.setViewName("errorView");
            return mv;
        }
        ProductView productView = facade.getProduct(id);
        mv.getModelMap().addAttribute("products", Arrays.asList(productView));
        mv.setViewName("product/showAllProducts");
        return mv;
    }

}
